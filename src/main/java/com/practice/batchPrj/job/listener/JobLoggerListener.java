package com.practice.batchPrj.job.listener;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.JobRepository;

@Slf4j
@RequiredArgsConstructor
public class JobLoggerListener implements JobExecutionListener {

    private final JobRepository jobRepository;
    private static final String BEFORE_MESSAGE = "{} job is running";
    private static final String AFTER_MESSAGE = "{} job is done. (status: {})";

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(BEFORE_MESSAGE, jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(AFTER_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
        if(jobExecution.getStatus().equals(BatchStatus.FAILED)){
            System.out.println("[SYSTEM] >>>>> job is failed");
        }
        // Execution : 현재 작업명, 상태 등을 나타내는 객체
        // JobRepository 객체를 활용해서 데이터를 가져옴
        // 배치 스케줄링 프로그램 등에서 활용할 수 있음
        JobExecution lastJobExecution = jobRepository.getLastJobExecution(
                /*JOB_NAME*/   jobExecution.getJobInstance().getJobName(),
                /*JOB_PARAMS*/ jobExecution.getJobParameters());
        if(lastJobExecution != null){
            for(StepExecution stepExecution : lastJobExecution.getStepExecutions()){
                System.out.println(stepExecution.getStatus());
                System.out.println(stepExecution.getStepName());
                System.out.println(stepExecution.getExitStatus());
            }
        }
    }
}
