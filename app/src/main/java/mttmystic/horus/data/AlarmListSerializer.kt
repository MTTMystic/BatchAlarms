package mttmystic.horus.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import mttmystic.horus.proto.AlarmList
import java.io.InputStream
import java.io.OutputStream

object AlarmListSerializer : Serializer<mttmystic.horus.proto.AlarmList> {
    override val defaultValue : mttmystic.horus.proto.AlarmList = mttmystic.horus.proto.AlarmList.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): mttmystic.horus.proto.AlarmList {
        try {
            return mttmystic.horus.proto.AlarmList.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AlarmList, output: OutputStream) {
        t.writeTo(output)
    }
}