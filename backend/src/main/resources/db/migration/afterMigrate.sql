INSERT INTO companies(name, business_number) VALUES ('원티드랩', 2998600021);
INSERT INTO companies(name, business_number) VALUES ('(주)사람인에이치알', 1138600917);
INSERT INTO skills(name) VALUES ('java'), ('spring'), ('react'), ('javascript');
INSERT INTO posts(position_name, job_description, reward, company_id) VALUES
('백엔드 주니어 개발자', '원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은 java, ...', 1500000, 1),
('프론트 주니어 개발자', '사람인에서 프론트 주니어 개발자를 채용합니다. 자격요건은 react, ...', 1000000, 2);
INSERT INTO position_skills(post_id, skill_id) VALUES (1, 2), (2, 4);