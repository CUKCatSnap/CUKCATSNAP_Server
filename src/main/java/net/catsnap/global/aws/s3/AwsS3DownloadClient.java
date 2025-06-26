package net.catsnap.global.aws.s3;

public abstract class AwsS3DownloadClient implements ImageDownloadClient {

    private final AwsS3Properties awsS3Properties;

    protected AwsS3DownloadClient(AwsS3Properties awsS3Properties) {
        this.awsS3Properties = awsS3Properties;
    }

    @Override
    public String getDownloadImageUrl(String savedFileName) {
        String bucketDirectory = addBucketDirectory(savedFileName);
        return "https://" + awsS3Properties.getBucketName()
            + ".s3." + awsS3Properties.getRegion()
            + ".amazonaws.com/" + bucketDirectory;
    }

    private String addBucketDirectory(String fileName) {
        return awsS3Properties.getRawImageFolder() + "/" + fileName;
    }
}
