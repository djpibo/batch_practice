package com.practice.batchPrj.job.dataRW;

import com.practice.batchPrj.core.dto.Orders;
import com.practice.batchPrj.core.dto.Settlements;
import com.practice.batchPrj.core.repository.OrderRepository;
import com.practice.batchPrj.core.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class TrMigrationJobConfig {
    private static final String JOB_NAME = "trMigrationJob"; // Application > JobParameter에 설정할 값
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final OrderRepository orderRepository;
    private final SettlementRepository settlementRepository;

    @Bean
    @Primary
    public Job trMigrationJob(Step trMigrationStep){
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(trMigrationStep)
                .build();
    }

    @JobScope
    @Bean
    public Step trMigrationStep(ItemReader<Orders> trOrdersReader){
        return stepBuilderFactory.get(JOB_NAME+"_step")
                .<Orders, Settlements>chunk(5)   // chunk 단위로 모아서 commit
                .reader(trOrdersReader)
                .processor(toSettleProcessor())
                .writer(trSettleWriter())
                /*
                .writer(new ItemWriter() {
                    @Override
                    public void write(List items) throws Exception {
                        items.forEach(System.out::println);
                    }
                })
                */
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Orders> trOrdersReader(){
        return new RepositoryItemReaderBuilder<Orders>()
                .name(JOB_NAME+"_reader")
                .repository(orderRepository)
                .methodName("findAll")
                .pageSize(5)
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Orders, Settlements> toSettleProcessor(){
        return new ItemProcessor<Orders, Settlements>() {
            @Override
            public Settlements process(Orders item) throws Exception {
                return new Settlements(item);
            }
        };
    }

    @StepScope
    @Bean
    public RepositoryItemWriter<Settlements> trSettleWriter(){
        return new RepositoryItemWriterBuilder<Settlements>()
                .repository(settlementRepository)
                .methodName("save")
                .build();
    }

    @StepScope
    @Bean
    public ItemWriter<Settlements> trSettlementsItemWriter(){
        return new ItemWriter<Settlements>() {
            @Override
            public void write(List<? extends Settlements> items) throws Exception {
                items.forEach(settlementRepository::save);
            }
        };
    }
}
