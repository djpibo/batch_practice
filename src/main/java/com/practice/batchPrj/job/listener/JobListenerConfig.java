package com.practice.batchPrj.job.listener;

import com.practice.batchPrj.job.validatedParam.validator.FileNameParamValidator;
import com.practice.batchPrj.job.validatedParam.validator.FileParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/*
	- before
	- after
 */
@Configuration
@RequiredArgsConstructor
public class JobListenerConfig {
	private static final String JOB_NAME = "JobListener";

	private final StepBuilderFactory stepBuilderFactory;

	private final JobBuilderFactory jobBuilderFactory;
	private final JobRepository jobRepository;

	@Bean
	public Job jobListenerJob(Step jobListenerStep){  // 내부 클래스에서 직접 찾아서 호출하는 방식이 아닌, 전달인자에 추가하는 방법 = Bean을 통한 주입방식
		return jobBuilderFactory.get(JOB_NAME)
				.incrementer(new RunIdIncrementer())
				.listener(new JobLoggerListener(jobRepository))
				//.validator(new FileParamValidator())
				//.validator(multipleValidator())
				.start(jobListenerStep)
				.build();
	}

	// 2개 이상의 유효성 검증이 필요한 경우
	private CompositeJobParametersValidator multipleValidator(){
		CompositeJobParametersValidator compositeJobParametersValidator = new CompositeJobParametersValidator();
		compositeJobParametersValidator.setValidators(Arrays.asList(new FileParamValidator(), new FileNameParamValidator()));
		return compositeJobParametersValidator;
	}

	@JobScope
	@Bean
	public Step jobListenerStep(Tasklet jobListenerTasklet){
		return stepBuilderFactory.get(JOB_NAME+"_step")
				.tasklet(jobListenerTasklet)
				.build();
	}

	@StepScope
	@Bean
	public Tasklet jobListenerTasklet(@Value("#{jobParameters['filename']}") String filename){ // -filename=test.csv
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("[SYSTEM] >>>>> JobListener executed. >>>>> " + filename);
				return RepeatStatus.FINISHED;
				// throw new Exception();
			}
		};
	}
}
