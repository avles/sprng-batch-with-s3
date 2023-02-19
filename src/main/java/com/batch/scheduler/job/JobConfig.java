package com.batch.scheduler.job;

import com.batch.scheduler.s3.S3BucketStorageService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    S3BucketStorageService service;


    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(startStep())
                .next(downloadFromS3())
                .next(lastStep())
                .build();
    }

    @Bean
    public Step startStep() {
        return stepBuilderFactory.get("startStep")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Start Step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step downloadFromS3() {
        return stepBuilderFactory.get("downloadFromS3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Initiate download from S3");
                    System.out.println("Files downloaded:" + service.downloadFiles());
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step lastStep() {
        return stepBuilderFactory.get("lastStep")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Last Step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
