package me.whypie.domain.member.service

import me.whypie.domain.member.model.dto.request.CertificationVerifyRequest
import me.whypie.domain.rds.member.repository.MemberRepo
import me.whypie.domain.redis.member.model.entity.CertificationMailCache
import me.whypie.domain.redis.member.repository.CertificationMailCacheRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author : choi-ys
 * @date : 2021-10-04 오전 5:16
 */
@Service
@Transactional(readOnly = true)
// TODO: 용석(2021-10-04) : 예외 처리 문구 정리
class MemberCertificationVerifyService(
    private val memberRepo: MemberRepo,
    private val mailService: MailService,
    private val certificationMailRepo: CertificationMailCacheRepo
) {

    // TODO: 용석(2021-10-04) : 발송 여부에 따른 응답 처리
    fun sendCertificationMail(id: Long) {
        val member = memberRepo.findById(id).orElseThrow() {
            throw IllegalArgumentException("")
        }

        val certificationNumber = mailService.sendSignupCertificationMail(member)

        certificationMailRepo.save(
            CertificationMailCache(
                email = member.email,
                certificationNumber = certificationNumber
            )
        )
    }

    @Transactional
    fun verifyCertification(certificationVerifyRequest: CertificationVerifyRequest) {
        val certificationMailCache = certificationMailRepo.findById(certificationVerifyRequest.email).orElseThrow() {
            throw IllegalArgumentException("") // 인증 번호 발송 내역이 Redis에 존재 하지 않는 경우
        }
        certificationMailCache.let {
            if (it.certificationNumber != certificationVerifyRequest.certificationNumber) { // 발송된 인증번호와 입력된 인증번호가 다른 경우
                throw IllegalArgumentException("")
            }
        }

        val member = memberRepo.findById(certificationVerifyRequest.id).orElseThrow() {
            throw IllegalArgumentException("")
        }
        member.completeCertification()
    }
}