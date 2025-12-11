package mttmystic.batchAlarms.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import batchAlarms.proto.AlarmList
import java.io.InputStream
import java.io.OutputStream

object AlarmListSerializer : Serializer<batchAlarms.proto.AlarmList> {
    override val defaultValue : batchAlarms.proto.AlarmList = batchAlarms.proto.AlarmList.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): batchAlarms.proto.AlarmList {
        try {
            return batchAlarms.proto.AlarmList.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AlarmList, output: OutputStream) {
        t.writeTo(output)
    }
}