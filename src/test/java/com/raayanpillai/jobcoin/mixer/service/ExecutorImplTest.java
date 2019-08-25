package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.model.Address;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
public class ExecutorImplTest {

    private VirtualTimeScheduler virtualTimeScheduler;
    private Address houseAddress;
    private long minDelay;
    private long maxDelay;

    @MockBean
    private Transfer transfer;

    private ExecutorImpl executor;

    @Before
    public void setUp() throws Exception {
        virtualTimeScheduler = VirtualTimeScheduler.create();
        houseAddress = new Address("testHouse");
        minDelay = 0;
        maxDelay = 10;
        executor = new ExecutorImpl(transfer, virtualTimeScheduler, houseAddress, minDelay, maxDelay);
    }

    @Test
    public void scheduleWithdrawal_maxDelay_transferAttempted() {
        given(transfer.move(any(Address.class), any(Address.class), anyFloat()))
                .willReturn(true);

        Float amount = 100F;
        Address destinationAddress = new Address("testOutput");

        executor.scheduleWithdrawal(destinationAddress, amount);

        virtualTimeScheduler.advanceTimeBy(Duration.ofSeconds(maxDelay));

        then(transfer).should().move(any(Address.class), any(Address.class), anyFloat());
    }

    @Test
    public void scheduleWithdrawal_minDelay_noInteraction() {
        given(transfer.move(any(Address.class), any(Address.class), anyFloat()))
                .willReturn(true);

        executor.scheduleWithdrawal(new Address("testOutput"), 100F);

        virtualTimeScheduler.advanceTimeBy(Duration.ofSeconds(minDelay));

        then(transfer).shouldHaveZeroInteractions();
    }
}