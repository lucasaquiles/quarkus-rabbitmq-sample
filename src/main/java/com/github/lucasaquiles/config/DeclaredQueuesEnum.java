package com.github.lucasaquiles.config;

public enum DeclaredQueuesEnum {

    SAMPLE_QUEUE("queue-sample", false, 5, 5, RetryStrategyEnum.NONE),
    SAMPLE_QUEUE_TWO("sample-queue-two", true, 5, 5, RetryStrategyEnum.NONE);

    private String queueName;
    private String exchangeName;

    private boolean withDlq;
    private int maxRetry;
    private int interval;
    private RetryStrategyEnum retryStrategyEnum;

    DeclaredQueuesEnum(String queueName, boolean withDlq, int maxRetry, int interval, RetryStrategyEnum retryStrategyEnum) {
        this.queueName = queueName;
        this.withDlq = withDlq;
        this.maxRetry = maxRetry;
        this.interval = interval;
        this.retryStrategyEnum = retryStrategyEnum;
    }

    public String getExchangeName() {
        return this.queueName + ".exchange";
    }

    public String getRetryName() {
        return this.queueName + ".retry";
    }

    public String getDLQName() {
        return this.getQueueName() + ".dlq";
    }

    public boolean isRetryableQueue() {
        return maxRetry > 0;
    }

    public String getQueueName() {
        return queueName;
    }

    public boolean isWithDlq() {
        return withDlq;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public int getInterval() {
        return interval;
    }

    public RetryStrategyEnum getRetryStrategyEnum() {
        return retryStrategyEnum;
    }

    @Override
    public String toString() {
        return "DeclaredQueuesEnum{" +
                "queueName='" + queueName + '\'' +
                ", exchangeName='" + exchangeName + '\'' +
                ", withDlq=" + withDlq +
                ", maxRetry=" + maxRetry +
                ", interval=" + interval +
                ", retryStrategyEnum=" + retryStrategyEnum +
                '}';
    }
}


enum RetryStrategyEnum {
    LINEAR, EXPONENCIAL, NONE
}
