package no.nav.pensjon.vtp.mocks.tp

import org.springframework.stereotype.Service

@Service
class SequenceService(private val sequenceRepository: SequenceRepository) {
    fun getNextVal(name: String) = sequenceRepository.save(
        sequenceRepository.findByName(name)
            ?.run { copy(value = value + 1) }
            ?: Sequence(name)
    ).value
}
