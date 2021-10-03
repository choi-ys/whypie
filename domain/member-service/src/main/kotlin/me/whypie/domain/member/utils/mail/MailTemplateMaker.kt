package me.whypie.domain.member.utils.mail

import org.springframework.mail.SimpleMailMessage

/**
 * @author : choi-ys
 * @date : 2021-10-04 오전 6:23
 */
class MailTemplateMaker {

    companion object {

        // TODO: 용석(2021-10-04) : MimeMessage(HTML) template로 변경
        fun signupCertificationMailTemplate(email: String, certificationNumber: Int): SimpleMailMessage {
            val title = "[WhyPie] 회원 가입 인증 메일"

            return SimpleMailMessage().also {
                it.setTo(email)
                it.setSubject(title)
                it.setText(certificationNumber.toString())
            }
        }
    }
}