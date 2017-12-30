package codes.rik.window.lambda.common

import codes.rik.window.util.Arn
import codes.rik.window.util.gson
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.annotations.SerializedName

data class S3Input(
        @SerializedName("Records") val records: List<Record>) {

    companion object {
        fun read(string: String): S3Input = gson.fromJson(string)
    }

    data class Record(
        val s3: S3Info) {

        data class S3Info(
                val `object`: S3Object,
                val bucket: S3Bucket) {

            data class S3Object(val key: String, val size: Int)
            data class S3Bucket(val arn: String, val name: String) {
                fun toBucket() = codes.rik.window.util.S3Bucket(Arn(arn), name)
            }
        }
    }
}

