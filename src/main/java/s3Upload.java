import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class s3Upload {
    public static void main(String[] args) throws IOException {
        // 設定変数一覧（環境変数定義予定）
        // tmpディレクトリパス
        String tmpDirPath = "/Users/otkshol/Desktop/test";
        String s3UploadPath = "tmp/create_kpi_data_for_client_success/";
        String accessKey = "";
        String secretKey = "";

        // TODO tmpファイルパスは環境変数としてもたせる
        Path dirPath = Paths.get(tmpDirPath);
        // TODO アクセスキーとシークレットキーが環境毎にことなるのでpropertyファイルに持たせる
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        // S3クライアントの生成
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                // TODO リージョンの確認 AP_NORTHEAST_1であるか
                .withRegion(Regions.AP_NORTHEAST_1)
                .build();
        // ファイルをS3へアップロード
        Files.list(dirPath)
                .forEach(filePath ->
                        s3Client.putObject("bucketName", s3UploadPath + filePath.getFileName().toString(), filePath.toFile()));
        // アップロード後にファイル削除
        Files.delete(Paths.get(tmpDirPath));
    }
}