package me.whypie.service

import me.whypie.domain.generator.MemberGenerator
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

/**
 * @author : choi-ys
 * @date : 2021-10-04 오전 7:40
 */
@ExtendWith(MockitoExtension::class)
@DisplayName("Service:Mail")
internal class MailServiceTest {
    @Mock
    lateinit var javaMailSender: JavaMailSender

    @InjectMocks
    lateinit var mailService: MailService

    @Test
    @DisplayName("회원 인증 메일 전송")
    fun send() {
        // Given
        val member = MemberGenerator.member()

        BDDMockito.doNothing().`when`(javaMailSender).send(BDDMockito.any(SimpleMailMessage::class.java))

        // When
        val sentCertificationNumber = mailService.sendSignupCertificationMail(member)

        // Then
        verify(javaMailSender, BDDMockito.times(1)).send(BDDMockito.any(SimpleMailMessage::class.java))
        assertNotEquals(sentCertificationNumber, 0L)
    }
}