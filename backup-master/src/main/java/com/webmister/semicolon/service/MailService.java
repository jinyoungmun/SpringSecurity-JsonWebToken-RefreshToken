package com.webmister.semicolon.service;

import com.webmister.semicolon.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    final UserInfoRepository userInfoRepository;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String sender;

    public void mailSend(String userEmail, String key) throws Exception {
        String userNickName = userInfoRepository.findByUserEmail(userEmail).get().getUserNickName();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(userEmail);
        message.setSubject("세미콜론 : 회원가입을 완료해주세요.");
        message.setText("아래 링크를 클릭해 회원가입을 완료해주세요 -> " + System.lineSeparator() +
                "http://localhost:8081/api/signup/" + userNickName + "/" + key);
        log.info(String.valueOf(message));
        mailSender.send(message);
    }

}
