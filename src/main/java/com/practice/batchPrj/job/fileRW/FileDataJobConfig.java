package com.practice.batchPrj.job.fileRW;

import com.practice.batchPrj.core.dto.Player;
import com.practice.batchPrj.core.dto.PlayerYears;
import com.practice.batchPrj.core.mapper.PlayerFieldSetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class FileDataJobConfig {
    private static final String JOB_NAME = "fileDataJob";
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job fileDataJob(Step fileDataStep){  // Spring은 메소드 이름으로 가져오기 때문에 반드시 전달인자와 Bean으로 등록한 메소드 이름이 일치해야 함
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(fileDataStep)
                .build();
    }

    @JobScope
    @Bean
    public Step fileDataStep(FlatFileItemReader playerFlatFileItemReader, ItemProcessor playerYearsItemProcessor, FlatFileItemWriter playerYearsItemWriter){
        return stepBuilderFactory.get(JOB_NAME+"_step")
                .<Player, PlayerYears>chunk(5)
                .reader(playerFlatFileItemReader)
                .processor(playerYearsItemProcessor)
                .writer(playerYearsItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemReader<Player> playerFlatFileItemReader(){
        return new FlatFileItemReaderBuilder<Player>()
                .name(JOB_NAME+"_reader")
                .resource(new FileSystemResource("Player.csv"))
                .lineTokenizer(new DelimitedLineTokenizer())
                .fieldSetMapper(new PlayerFieldSetMapper())
                .linesToSkip(1)
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Player, PlayerYears> playerYearsItemProcessor(){
        return new ItemProcessor<Player, PlayerYears>() {
            @Override
            public PlayerYears process(Player item) throws Exception {
                return new PlayerYears(item);
            }
        };
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<PlayerYears> playerYearsItemWriter() {
        BeanWrapperFieldExtractor<PlayerYears> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"playerId", "playerName", "teamName", "position", "age", "years"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<PlayerYears> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FileSystemResource fileSystemResource = new FileSystemResource("Players_years.txt");

        return new FlatFileItemWriterBuilder<PlayerYears>()
                .name(JOB_NAME+"_writer")
                .lineAggregator(lineAggregator)
                .resource(fileSystemResource)
                .build();
        }
    }
