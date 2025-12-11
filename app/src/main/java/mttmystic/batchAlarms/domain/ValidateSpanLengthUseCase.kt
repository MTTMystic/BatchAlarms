package mttmystic.batchAlarms.domain

import jakarta.inject.Inject
import mttmystic.batchAlarms.data.Interval
import mttmystic.batchAlarms.data.Span

class ValidateSpanLengthUseCase @Inject constructor() {
    operator fun invoke(span: Span, interval: Interval) : Boolean {
        return span.lengthInMillis() >= interval.inMillis()
    }
}