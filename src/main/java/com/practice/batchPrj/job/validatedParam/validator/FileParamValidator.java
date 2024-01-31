package com.practice.batchPrj.job.validatedParam.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class FileParamValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String fileName = Objects.requireNonNull(parameters).getString("filename");
        if(!StringUtils.endsWithIgnoreCase(fileName, ".csv")){
            throw new JobParametersInvalidException("JobParametersInvalidException [suffix = csv] ");
        }
    }
}
