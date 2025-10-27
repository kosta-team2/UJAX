drop database ujax;
CREATE DATABASE ujax;
USE ujax;

CREATE TABLE workspace_member
(
    ws_member_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ws_id        BIGINT      NOT NULL,
    member_id    BIGINT      NOT NULL,
    is_leader    TINYINT(1)  NOT NULL,
    created_at   DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at   DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    is_deleted   TINYINT(1)  NOT NULL DEFAULT 0,
    email        VARCHAR(30) NOT NULL,
    nickname     VARCHAR(30) NOT NULL
);

CREATE TABLE problem
(
    problem_id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    problem_num      INT          NOT NULL UNIQUE,
    title            VARCHAR(255) NOT NULL,
    tier             VARCHAR(50)  NOT NULL,
    time_limit_raw   VARCHAR(50)  NULL,
    memory_limit_raw VARCHAR(50)  NULL,
    problem_desc     TEXT         NOT NULL,
    problem_input    TEXT         NOT NULL,
    problem_output   TEXT         NOT NULL,
    url              VARCHAR(255) NOT NULL
);

CREATE TABLE problem_algorithm
(
    algorithm_id INT    NOT NULL,
    problem_id   BIGINT NOT NULL,
    PRIMARY KEY (algorithm_id, problem_id)
);

CREATE TABLE token
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id     BIGINT       NOT NULL,
    refresh_token VARCHAR(512) NOT NULL UNIQUE,
    created_at    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
);

ALTER TABLE token
    ADD COLUMN expires_at DATETIME(3) NOT NULL AFTER refresh_token,
    ADD COLUMN revoked_at DATETIME(3) NULL AFTER expires_at;

CREATE TABLE EmailAlert
(
    alert_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    ws_member_id  BIGINT                                        NOT NULL,
    ws_problem_id VARCHAR(255)                                  NOT NULL,
    status        ENUM ('SCHEDULED','SENT','CANCELED','FAILED') NOT NULL,
    created_at    DATETIME(3)                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
);

CREATE TABLE solution
(
    solution_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    ws_problem_id BIGINT      NOT NULL,
    ws_member_id  BIGINT      NOT NULL,
    status        TINYINT(1)  NOT NULL,
    time_ms       INT         NULL     DEFAULT 0,
    memory_mb     INT         NULL     DEFAULT 0,
    code          TEXT        NOT NULL,
    created_at    DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at    DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    is_deleted    TINYINT(1)  NOT NULL DEFAULT 0
);

-- _base_template 테이블은 제거 요청에 따라 생성하지 않음

CREATE TABLE workspace_problem
(
    ws_problem_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ws_id         BIGINT      NOT NULL,
    problem_id    BIGINT      NOT NULL,
    deadline      DATETIME(3) NOT NULL,
    scheduled_at  DATETIME(3) NULL,
    created_at    DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at    DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    is_deleted    TINYINT(1)  NOT NULL DEFAULT 0
);

ALTER TABLE workspace_problem
    ADD CONSTRAINT uk_ws_problem UNIQUE (ws_id, problem_id);

CREATE TABLE workspace
(
    ws_id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    ws_name      VARCHAR(30) NOT NULL,
    ws_lang      VARCHAR(30) NOT NULL, -- 원문 ENUM(...)은 실제 값 확정 전이라 VARCHAR로 처리
    is_hint_view TINYINT(1)  NOT NULL,
    created_at   DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at   DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    is_deleted   TINYINT(1)  NOT NULL DEFAULT 0
);

CREATE TABLE reward
(
    reward_id  VARCHAR(255) PRIMARY KEY,
    member_id  BIGINT      NOT NULL,
    product_id BIGINT      NOT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
);

CREATE TABLE gift
(
    product_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name  VARCHAR(50)   NOT NULL,
    product_price BIGINT        NOT NULL,
    product_image VARCHAR(2048) NOT NULL,
    created_at    DATETIME(3)   NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
);


CREATE TABLE barcode
(
    barcode_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id    BIGINT        NOT NULL,
    barcode_image VARCHAR(2048) NOT NULL,
    status        TINYINT       NOT NULL DEFAULT 0,
    created_at    DATETIME(3)   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at    DATETIME(3)   NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
);

CREATE TABLE algorithm
(
    algorithm_id   INT PRIMARY KEY AUTO_INCREMENT,
    algorithm_name VARCHAR(50) NOT NULL
);

CREATE TABLE likes
(
    like_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    solution_id  BIGINT      NOT NULL,
    ws_member_id BIGINT      NOT NULL,
    created_at   DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
);

CREATE TABLE sample
(
    sample_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
    problem_id    BIGINT NOT NULL,
    sample_index  INT    NOT NULL,
    sample_input  TEXT   NOT NULL,
    sample_output TEXT   NOT NULL,
    UNIQUE KEY uq_sample_problem_idx (problem_id, sample_index)
);

CREATE TABLE notice
(
    n_id       BIGINT      NOT NULL AUTO_INCREMENT,
    ws_id      BIGINT      NOT NULL,
    n_title    VARCHAR(50) NOT NULL,
    n_content  TEXT        NOT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    PRIMARY KEY (n_id, ws_id)
);

CREATE TABLE member
(
    member_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(60)  NOT NULL,
    nickname   VARCHAR(30)  NOT NULL UNIQUE,
    reward     BIGINT       NOT NULL DEFAULT 0,
    xp         INT          NOT NULL DEFAULT 0,
    created_at DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    is_deleted TINYINT(1)   NOT NULL DEFAULT 0
);

CREATE TABLE tier
(
    tier_id   INT PRIMARY KEY AUTO_INCREMENT,
    tier_name VARCHAR(50) NOT NULL,
    min_xp    INT         NOT NULL,
    max_xp    INT         NOT NULL
);

CREATE TABLE comment
(
    comment_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    solution_id     BIGINT      NOT NULL,
    ws_member_id    BIGINT      NOT NULL,
    comment_content TEXT        NOT NULL,
    created_at      DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
);

-- Foreign Keys (원문에 명시된 것만 반영)
-- problem_algorithm FK
ALTER TABLE problem_algorithm
    ADD CONSTRAINT fk_pa_algo FOREIGN KEY (algorithm_id) REFERENCES algorithm (algorithm_id),
    ADD CONSTRAINT fk_pa_prob FOREIGN KEY (problem_id) REFERENCES problem (problem_id);

-- barcode FK (gift와 타입 일치)
ALTER TABLE barcode
    ADD CONSTRAINT fk_barcode_gift FOREIGN KEY (product_id) REFERENCES gift (product_id);

-- sample FK
ALTER TABLE sample
    ADD CONSTRAINT fk_sample_problem FOREIGN KEY (problem_id) REFERENCES problem (problem_id);

-- notice FK
ALTER TABLE notice
    ADD CONSTRAINT fk_notice_ws FOREIGN KEY (ws_id) REFERENCES workspace (ws_id);

-- ADDED: workspace_member FK/UNIQUE
ALTER TABLE workspace_member
    ADD CONSTRAINT fk_wm_ws FOREIGN KEY (ws_id) REFERENCES workspace (ws_id),
    ADD CONSTRAINT fk_wm_member FOREIGN KEY (member_id) REFERENCES member (member_id),
    ADD UNIQUE KEY uq_wm_ws_member (ws_id, member_id);
-- ADDED: 중복 가입 방지

-- ADDED: workspace_problem FK
ALTER TABLE workspace_problem
    ADD CONSTRAINT fk_wp_ws FOREIGN KEY (ws_id) REFERENCES workspace (ws_id),
    ADD CONSTRAINT fk_wp_problem FOREIGN KEY (problem_id) REFERENCES problem (problem_id);

-- ADDED: solution FK
ALTER TABLE solution
    ADD CONSTRAINT fk_sol_wp FOREIGN KEY (ws_problem_id) REFERENCES workspace_problem (ws_problem_id),
    ADD CONSTRAINT fk_sol_wm FOREIGN KEY (ws_member_id) REFERENCES workspace_member (ws_member_id);

-- ADDED: likes FK/UNIQUE
ALTER TABLE likes
    ADD CONSTRAINT fk_likes_sol FOREIGN KEY (solution_id) REFERENCES solution (solution_id),
    ADD CONSTRAINT fk_likes_wm FOREIGN KEY (ws_member_id) REFERENCES workspace_member (ws_member_id),
    ADD UNIQUE KEY uq_likes_one_per_member (solution_id, ws_member_id);
-- ADDED: 중복 좋아요 방지

-- ADDED: comment FK
ALTER TABLE comment
    ADD CONSTRAINT fk_comment_sol FOREIGN KEY (solution_id) REFERENCES solution (solution_id),
    ADD CONSTRAINT fk_comment_wm FOREIGN KEY (ws_member_id) REFERENCES workspace_member (ws_member_id);

-- ADDED: token FK
ALTER TABLE token
    ADD CONSTRAINT fk_token_member FOREIGN KEY (member_id) REFERENCES member (member_id);

ALTER TABLE algorithm
    ADD CONSTRAINT uq_algorithm_name UNIQUE (algorithm_name);


INSERT INTO member (email, password, nickname, reward, xp)
VALUES ('test@test.com', '123123123', 'test', 9999999, 999999),
       ('alice@example.com', 'pass1234', 'alice', 0, 120),
       ('bob@example.com', 'pass1234', 'bob', 5, 80),
       ('carol@example.com', 'pass1234', 'carol', 10, 200),
       ('dave@example.com', 'pass1234', 'dave', 0, 40),
       ('erin@example.com', 'pass1234', 'erin', 2, 65),
       ('frank@example.com', 'pass1234', 'frank', 0, 300),
       ('grace@example.com', 'pass1234', 'grace', 1, 150),
       ('heidi@example.com', 'pass1234', 'heidi', 0, 95),
       ('ivan@example.com', 'pass1234', 'ivan', 12, 220),
       ('judy@example.com', 'pass1234', 'judy', 0, 10);

INSERT INTO workspace (ws_name, ws_lang, is_hint_view)
VALUES ('WS01', 'JAVA11', 1),
       ('WS02', 'JAVA11', 0),
       ('WS03', 'JAVA11', 0),
       ('WS04', 'JAVA11', 1),
       ('WS05', 'JAVA11', 0);

INSERT INTO workspace_member (ws_id, member_id, is_leader, email, nickname)
values (1, 1, 1, 'test@test.com', 'test'),
       (2, 1, 1, 'test@test.com', 'test'),
       (3, 1, 1, 'test@test.com', 'test'),
       (4, 1, 0, 'test@test.com', 'test'),
       (5, 1, 0, 'test@test.com', 'test');

insert into notice (ws_id, n_title, n_content)
values (1, '공지입니다1', '공지 내용은 없습니다.'),
       (1, '공지입니다2', '공지 내용은 없습니다.'),
       (1, '공지입니다3', '공지 내용은 없습니다.'),
       (1, '공지입니다4', '공지 내용은 없습니다.'),
       (1, '공지입니다5', '공지 내용은 없습니다.'),
       (1, '공지입니다6', '공지 내용은 없습니다.'),
       (1, '공지입니다7', '공지 내용은 없습니다.'),
       (1, '공지입니다8', '공지 내용은 없습니다.'),
       (1, '공지입니다9', '공지 내용은 없습니다.'),
       (1, '공지입니다0', '공지 내용은 없습니다.');

INSERT INTO gift (product_name, product_price, product_image)
VALUES ('스타벅스 아이스아메리카노', 4500,
        'https://lh3.googleusercontent.com/rd-d/ALs6j_Hvc12z34gn-WWmLfVSR8wFEHC0VvV6UHmAE95q0qiksGZePJffAfUaCzJt-73dq3ES2AI4pvbcqxqMkmpiPaE28-ww6Hy49t5Ib1QGzQXQ_jpai6gMIxeQGAAmswDMw4GobJHt27iWIwMzwrlfkG12qjI7phjY0h_d2EHcvYR7wcoiqVUi_JjCxtNyvfJmXCvah8LWN7PfsYMkNSV_9aibous_A2hiEhM-9RMC7_43BzkLkcNSF7tCva-8rEryml_5LI1_unERYyagMjAry_h0t_2hh-i0lrIoGwgYaR1nOqLG0Gr9GMtxII61dXPbjqkZGVj1FlbL_JLV0iS7D0U31EGarbVhPRFswnnE82FNE1-Q-Qv022m6VN7OS-Njbo_vXvtiSYO3YZCzaP_c26kBHAxTyosFtRLfHjOXA0VO1tQq71JlDxRGw4WnNSRIrE6cUzUrzinaQzZTZ4m8GKEs3bAOipAq9vlcesWJy8iP9bh4xjfIu-dDaUGvHuY-yum_hIxV5vsYj2T6VOF0r2YCasH4lISThnDUiCCCrQV2wV_spBGjL8alE6Ve323u3mJkjspq5uTakMAv72PQSn5TGMyh6YOF7LiobH86O0pGOzXT0WA-7G8D4EcNDz1WrHg8veDDYtgo6E4Q-YJulG3Ujo8qIg0qVhugbozPEzi3KNDqdVKU26x7KZkGIpgD7UgSX2hMZLrYqCSbbqDLycuzdMxSa001PmXlBosj3W4KXw9cWAmShVSP2s3giXEOqwQuMcAcB2hwOQ12pFbq5sBVoDEDRyv12LkF9xV1IX9X5WHPQXzVs0kYpbF9fx1mgl2-XT6sTxJGi3uQ5r20CAuv7C-JwvDNrYnhBiRWj4Xdr7kLj1WoWtcJGog8cpwF8GIJ5k6PiWA3g-cPjbSu0pFtDbkV-KvuAs3e9txh0S_9Iw3cKQc85yRe1gmQtpuy95bYYEM2nzK5Xa4enHlrOrGtMEN2DtQGM5RuhEHwtiQ13tLDEVViAAiFDtw6Npfb0fNr_xYaaJpX3QmaH6V3LqVTyEOXJIK88J5UrTfGXBjmIfpGfdbi-MzN9oKJ0ih0kcXETK-V4nnd2aNa=w2560-h1317?auditContext=prefetch');
INSERT INTO gift (product_name, product_price, product_image)
VALUES ('BBQ 황금올리브 + 콜라1.5L', 25000,
        'https://lh3.googleusercontent.com/rd-d/ALs6j_F6rEtNxnaTjtLJVDbhIAnKJPd6kjO7mysTiFqNR6Wii7ikPcoTXKci2_1j8hlFekXxnKTHYkdoGx3AH5kDiR64pKAqIUqe54cYxEkRtlEOvvf3DauqSiZ4Yi-oJhOuOyixR3DQOv3NpkXJB6jGmYpIEAPSHCF5zZKQ-ZV9w7mhsq3O2U3eV8y51betMEqgmI2f4DA_Em9Mfl_1GM1y1m2ro8My-UweTEdPTnUhidSx05DIaoGVBJLyo19Gxv-aOtDtp94eGf8OFReIPhOa4O0nF-ox72BicqJy5fKg-2fX0yj-kVtfD7o6r88ZJUOd0ySGJyngl4MwBRGTWNa_dDbRHAGpu2H5Bh0kmmY0X-TuyxTdf4k9qU9wG1TmmD9K7UIYAn7xvtg9DkCNWQxkLD6DQpAwKcjvG8m2uY7OI1bpIGMyjQVbORbKZedXIY3v-B5YSbclAVe6U0tDieSmE5Tw0fwTGxwmjkbHKeI3HgUN_eYDMsbE1Ro5RKdWHaVtbmrZORyAZLXCvthFj5BXt5du3Hin5vrHxthejVgRNkOZskAW3bcIpODG-kOKZT3M0aP2MaGe_SRUgOtnx1Ri_AtZZ5c-WHgzIN8QU_s0GoVbQZ3mt711YXq894oKf4_uIArFuDoNFYiWe1d6HzrofW4TcR6hftdm12pdZn2ouoJHqmyyK9Rs2vRdpA7bnXypdZfgj6myxLRckzl3YT9pKNK2BG6WSS4Qkz6VZLycyKAdqLu4UK6qs7AkrdhlFvlqCPbf_OsCzQbboJyeMbxW5zfapqWlmX1Kns_Zy6rDC3rAC_eER84Hm7CHWJBWRt0uBbHrOB37hJx2jqqZVN8Ij6cZ7EheNWm_o2a_4jUUvgg0HQ1z7yGtz26GKlp7M-Gg4uBVuMUqSn-p1rwT4cQE6aHadsTIa050iv6LdDHcJ2l0pKd6a4R9i5FqA0NPKG1lTLJMwuGOUfS1bfgflE4V0Scn4TtF2f4L-7Y-IRjsoe62KmJAUs2_eUdL5YQ-w91ZDR2xr3ZlzRiKMey2JlpNdtbwJ0LdZ--Ix5t2kLgUji74tKnYlsxS0TK4uXREtvd8LYUJzX2vjqI5pZoAEg=w1195-h1317?auditContext=prefetch');
INSERT INTO gift (product_name, product_price, product_image)
VALUES ('맘스터치 싸이버거', 7500,
        'https://lh3.googleusercontent.com/rd-d/ALs6j_Hn40s9QGJ2mqPJBb2ZyTOwdutrQi3oSAUQdUMS_IGGmFrFPRQStP4wi5h8oIpuRivW-ZIMWIUGaxLwd0mVW3GPKr5fBYCOKref7kfuNDuAGttD-YngV3izIDoem3YaHSNE-SuM20zzh-72q7rEr5q6rjucpWb4_aRNZY4MMPV8SU1YljgkX2Q-F692acO4C_lSrYHtalcJxqWfECvSgMFkOrt4jL38OzNFcTMUNz5MHsV7HOT0NyxRv18D-D0cet6b98gllP3WB9OQDb9xiwyYCI7sjSkmGLZDFafjH5UsL_4opGo7Xt4NcC-ACPSwlGnj9cJD_bu92AKMGuU_QweC7czq6dK0Yfyaov5OdiPj8UQqnQ7ymxcrkcwpVRarHIYczQzet1-hJTcnG9ruNRI3q8jSwW6VqQaZiRNKHK8VRk0AvIWgFWqLYAH_rNjoy67vH1LFI1g37zGZ3qgsRkaT4Z_qREqnCVHu3CVmjGWarFgO3A7CWp5x0IhJl2wzIXV_u8kwvsCefaxhvb_nJVBUoea8lesv8-oROsfU-pncmQh7BNak9S6les0tjXQWIwqa_GtKlphJgjUct2C9Cu7_azwenltNDbpFu1Sml9DL0pr5g5s7Ebwz_hfC_qpiDjSMDJpDXP5savwm6ickIO1pVdiKrA1_XClH3w_o73S-htEQ3m5jXfVTHdgIiRxIb0XeysXXd4Lfq5Xx-_2c4SNYWVJxVE58PzyQhWizJ2WvHIhSf34d4nWBCIqSoaBQRFu2DpQ3CdNGdkTUrDZ3K7qcV9mQWd9UxTdbnDyseMt0WAV21-T7gg1g3AEzRFePNE7gpHlsX41OFErS0xMnRAlCHss7cx66m8O82JQXTWbIcN4G_L2-TKdpwOdnBBOL7xlmS2CNaiAGZ1cCYrHOuAn6FBgBP1h4NFnLbQbyf8e3ABhV1K8-gvDjfEz6qQvX1Ud0wSulGdBYYNlHrBTngKsJzmtlx3pwgQCXHNBvUpkrfp1swWW6AGAYcDLGbAqszg_DWTLZ1iUXs6wGKYTvMxnfMqw9VQW-7VIdznkouF1W7iZZhIzurBjAa-QqxObTKJ3X8glDVVRCt9Pvdg=w2560-h1317?auditContext=prefetch');

insert into barcode(barcode_image, product_id)
values ('https://drive.google.com/thumbnail?id=1qSG5K6_N-cIGU01FDguzh6aWEa-SND_e&sz=w1000', 1);
insert into barcode(barcode_image, product_id)
values ('https://drive.google.com/thumbnail?id=1qSG5K6_N-cIGU01FDguzh6aWEa-SND_e&sz=w1000', 2);
insert into barcode(barcode_image, product_id)
values ('https://drive.google.com/thumbnail?id=1qSG5K6_N-cIGU01FDguzh6aWEa-SND_e&sz=w1000', 3);
