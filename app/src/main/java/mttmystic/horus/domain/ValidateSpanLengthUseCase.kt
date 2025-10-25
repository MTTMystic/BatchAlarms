package mttmystic.horus.domain

import mttmystic.horus.data.Interval
import mttmystic.horus.data.Span

class ValidateSpanLengthUseCase {
    operator fun invoke(span: Span, interval: Interval) : Boolean {
        return span.lengthInMillis() >= interval.inMillis()
    }
}