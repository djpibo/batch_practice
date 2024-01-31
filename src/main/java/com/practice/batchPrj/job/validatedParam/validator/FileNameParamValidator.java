package com.practice.batchPrj.job.validatedParam.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class FileNameParamValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String fileName = Objects.requireNonNull(parameters).getString("filename");
        if(!StringUtils.startsWithIgnoreCase(fileName, "test")){
            throw new JobParametersInvalidException("JobParametersInvalidException [prefix = file] ");
        }
    }
}
