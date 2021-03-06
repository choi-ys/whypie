package me.whypie.service

import me.whypie.domain.model.entity.member.Member
import me.whypie.utils.mail.MailTemplateMaker.Companion.signupCertificationMailTemplate
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

/**
 * @author : choi-ys
 * @date : 2021-10-04 오전 7:06
 */
@Service
class MailService(
    private val javaMailSender: JavaMailSender,
) {

    // TODO: 용석(2021-10-04) : javaMailSender.send의 성공 여부와 발송 정보를(발송여부, 발송메일, 인증번호) 포함한 응답 처리
    fun sendSignupCertificationMail(member: Member): Int {
        val certificationNumber = (100000..200000).random()
        try {
            javaMailSender.send(signupCertificationMailTemplate(member.email, certificationNumber))
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("메일 발송에 실패하였습니다.")
        }
        return certificationNumber
    }
}