# CREATE DATABASE ujax;
# USE ujax;

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
    product_id    VARCHAR(255) PRIMARY KEY,
    product_name  VARCHAR(50)    NOT NULL,
    product_price DECIMAL(10, 2) NOT NULL,
    product_image BLOB           NOT NULL,
    created_at    DATETIME(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
);

CREATE TABLE barcode
(
    product_id    VARCHAR(255) PRIMARY KEY,
    barcode_image BLOB NOT NULL
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
    reward     INT          NOT NULL DEFAULT 0,
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
VALUES ('alice@example.com', 'pass1234', 'alice', 0.00, 120),
       ('bob@example.com', 'pass1234', 'bob', 5.50, 80),
       ('carol@example.com', 'pass1234', 'carol', 10.00, 200),
       ('dave@example.com', 'pass1234', 'dave', 0.00, 40),
       ('erin@example.com', 'pass1234', 'erin', 2.75, 65),
       ('frank@example.com', 'pass1234', 'frank', 0.00, 300),
       ('grace@example.com', 'pass1234', 'grace', 1.25, 150),
       ('heidi@example.com', 'pass1234', 'heidi', 0.00, 95),
       ('ivan@example.com', 'pass1234', 'ivan', 12.00, 220),
       ('judy@example.com', 'pass1234', 'judy', 0.00, 10);
INSERT INTO workspace_member (ws_id, member_id, is_leader, email, nickname)
VALUES (1, 2, 0, 'alice@example.com', 'alice');