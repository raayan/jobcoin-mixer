package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
public class ExecutorImplTest {

    private VirtualTimeScheduler virtualTimeScheduler;
    private long minDelay;
    private long maxDelay;

    @MockBean
    private Transfer transfer;

    private ExecutorImpl executor;

    @Before
    public void setUp() throws Exception {
        virtualTimeScheduler = VirtualTimeScheduler.create();
        minDelay = 10;
        maxDelay = 20;
        executor = new ExecutorImpl(transfer, virtualTimeScheduler, minDelay, maxDelay);
    }

    @Test
    public void scheduleWithdrawal_maxDelay_transferAttempted() {
        given(transfer.transact(any(Transaction.class))).willReturn(true);

        Transaction transaction = new Transaction(new Address("testOutput"), new Address("testHouse"), 100f);

        executor.scheduleTransaction(transaction);

        virtualTimeScheduler.advanceTimeBy(Duration.ofSeconds(maxDelay));

        then(transfer).should().transact(any(Transaction.class));
    }

    @Test
    public void scheduleWithdrawal_minDelay_noInteraction() {
        given(transfer.transact(any(Transaction.class)))
                .willReturn(true);

        Transaction transaction = new Transaction(new Address("testOutput"), new Address("testHouse"), 100f);

        executor.scheduleTransaction(transaction);

        virtualTimeScheduler.advanceTimeBy(Duration.ofSeconds(minDelay - 1));

        then(transfer).shouldHaveZeroInteractions();
    }
}