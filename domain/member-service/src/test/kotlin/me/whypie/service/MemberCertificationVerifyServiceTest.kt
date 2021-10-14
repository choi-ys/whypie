package me.whypie.service

import me.whypie.domain.generator.MemberGenerator
import me.whypie.model.dto.request.CertificationVerifyRequest
import me.whypie.model.entity.CertificationMailCache
import me.whypie.repository.CertificationMailCacheRepo
import me.whypie.domain.repository.MemberRepo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.AdditionalAnswers
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021-10-04 오전 7:15
 */
@ExtendWith(MockitoExtension::class)
@DisplayName("Service:MailCertify")
internal class MemberCertificationVerifyServiceTest {

    @Mock
    lateinit var memberRepo: MemberRepo

    @Mock
    lateinit var mailService: MailService

    @Mock
    lateinit var certificationMailRepo: CertificationMailCacheRepo

    @InjectMocks
    lateinit var memberCertificationVerifyService: MemberCertificationVerifyService

    @Test
    @DisplayName("회원 가입 인증 메일 전송")
    internal fun sendCertificationMail() {
        // Given
        val member = MemberGenerator.member()
        given(memberRepo.findById(anyLong()))
            .willReturn(Optional.of(member))

        val certificationNumber = (100000..200000).random()
        given(mailService.sendSignupCertificationMail(member))
            .willReturn(certificationNumber)

        given(certificationMailRepo.save(any(CertificationMailCache::class.java)))
            .will(AdditionalAnswers.returnsFirstArg<CertificationMailCache>())

        // When
        memberCertificationVerifyService.sendCertificationMail(0L)

        // Then
        verify(memberRepo, times(1)).findById(anyLong())
        verify(mailService, times(1)).sendSignupCertificationMail(member)
        verify(certificationMailRepo, times(1)).save(any(CertificationMailCache::class.java))
    }

    @Test
    @DisplayName("회원 가입 인증 번호 검증")
    internal fun verifyCertification() {
        // Given
        val email = "project.log.062@gmail.com"
        val certificateSerialNumber = (100000..200000).random()
        val certificationMailCache = CertificationMailCache(email, certificateSerialNumber)
        given(certificationMailRepo.findById(anyString()))
            .willReturn(Optional.of(certificationMailCache))

        val member = MemberGenerator.member()
        given(memberRepo.findById(anyLong())).willReturn(Optional.of(member))

        val certificationVerifyRequest = CertificationVerifyRequest(0L, email, certificateSerialNumber)

        // When
        memberCertificationVerifyService.verifyCertification(certificationVerifyRequest)

        // Then
        verify(certificationMailRepo, times(1)).findById(anyString())
        verify(memberRepo, times(1)).findById(anyLong())
    }
}
