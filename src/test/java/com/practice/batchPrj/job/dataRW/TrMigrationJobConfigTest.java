package com.practice.batchPrj.job.dataRW;
import org.junit.jupiter.api.Test;
import com.practice.batchPrj.core.dto.Orders;
import com.practice.batchPrj.core.repository.OrderRepository;
import com.practice.batchPrj.core.repository.SettlementRepository;
import com.practice.batchPrj.job.basic.SpringBatchTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {SpringBatchTestConfig.class, TrMigrationJobConfig.class})
public class TrMigrationJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SettlementRepository settlementRepository;

    @Test
    public void success_noData() throws Exception{
        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        //then
        Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(0, settlementRepository.count());
    }
    @Test
    public void success_withData() throws Exception{
        //given
        Orders orders1 = new Orders(10,"pico",LocalDate.now(),BigDecimal.valueOf(30000));
        Orders orders2 = new Orders(15,"pico",LocalDate.now(),BigDecimal.valueOf(30000));

        orderRepository.save(orders1);
        orderRepository.save(orders2);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(2, settlementRepository.count());
    }
}