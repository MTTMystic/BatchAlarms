package mttmystic.horus.domain

import jakarta.inject.Inject
import mttmystic.horus.data.Interval
import mttmystic.horus.data.Span

class ValidateSpanLengthUseCase @Inject constructor() {
    operator fun invoke(span: Span, interval: Interval) : Boolean {
        return span.lengthInMillis() >= interval.inMillis()
    }
}