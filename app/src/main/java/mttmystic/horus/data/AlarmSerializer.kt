package mttmystic.horus.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import mttmystic.horus.proto.Alarm
import java.io.OutputStream

//import mttmystic.horus.proto.AlarmOuterClass

object AlarmSerializer : Serializer<Alarm> {
    override val defaultValue : Alarm= Alarm.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): Alarm {
        try {
            return Alarm.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Alarm, output: OutputStream) {
        t.writeTo(output)
    }
}