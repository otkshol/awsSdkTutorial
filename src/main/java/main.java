import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;
import java.util.Arrays;

public class main {
    public static void main(String[] args) {
        getFiles();
       // uploadFiles();
        deleteFiles();
    }

    private static void getFiles() {
        // TODO tmpファイルパスは環境変数としてもたせる
        File dir = new File("/Users/otkshol/Desktop/test");
        File[] fileList =  dir.listFiles();
        if (fileList == null){
            // 警告終了する（ジョブネットリランさせる）
            return;
        }
        Arrays.stream(fileList).forEach(s -> System.out.println(s.getName()));
    }

    private static void uploadFiles() {

        // TODO tmpファイルパスは環境変数としてもたせる
        File dir = new File("/Users/otkshol/Desktop/test");
        File[] fileList =  dir.listFiles();
        // null制御する
        if (fileList == null){
            // 警告終了する（ジョブネットリランさせる）
            return;
        }
        // アクセスキーとシークレットキーが環境毎にことなるのでpropertyファイルに持たせる
        AWSCredentials credentials = new BasicAWSCredentials("アクセスキー","シークレットキー");

        // S3クライアントの生成
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                // TODO リージョンの確認 AP_NORTHEAST_1で大丈夫？？
                .withRegion(Regions.AP_NORTHEAST_1)
                .build();
        // ファイルをアップロード
        Arrays.stream(fileList)
                .forEach(uploadFile ->
                        s3Client.putObject("bucketName", "tmp/create_kpi_data_for_client_success/" + uploadFile.getName(), uploadFile));
        for (File file: fileList ){
            try{
                s3Client.putObject("bucketName", "tmp/create_kpi_data_for_client_success/" + file.getName(), file);
            } catch (AmazonServiceException e){
                e.printStackTrace();
            } catch (SdkClientException e) {
                e.printStackTrace();
            }
        }

        Arrays.stream(fileList).forEach(File::delete);
    }




    private static void deleteFiles() {
        File dir = new File("/Users/otkshol/Desktop/test");
        File[] fileList =  dir.listFiles();
        // null制御する
        if (fileList == null){
            // 警告終了する（ジョブネットリランさせる）
            return;
        }
        Arrays.stream(fileList).forEach(File::delete);
    }

}
