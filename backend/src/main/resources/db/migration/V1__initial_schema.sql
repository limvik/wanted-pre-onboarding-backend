CREATE TABLE `users` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE `companies` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `business_number` INT NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE `posts` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `company_id` INT NOT NULL,
    `position_name` VARCHAR(255) NOT NULL,
    `job_description` TEXT NOT NULL,
    `reward` INT NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE `addresses` (
    `post_id` INT NOT NULL,
    `street` VARCHAR(255) NOT NULL,
    `city` VARCHAR(255) NOT NULL,
    `state` VARCHAR(255) NOT NULL,
    PRIMARY KEY(post_id),
    FOREIGN KEY(post_id) REFERENCES posts(id) ON DELETE CASCADE
);

CREATE TABLE `skills` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE `position_skills` (
    `post_id` INT NOT NULL,
    `skill_id` INT NOT NULL,
    PRIMARY KEY(post_id, skill_id),
    FOREIGN KEY(post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY(skill_id) REFERENCES skills(id) ON DELETE CASCADE
);

CREATE TABLE `status` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE `applications` (
    `user_id` INT,
    `post_id` INT,
    `applied_at` DATETIME DEFAULT now(),
    `updated_at` DATETIME DEFAULT now(),
    `status_id` INT DEFAULT 1,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY(post_id) REFERENCES posts(id) ON DELETE SET NULL,
    FOREIGN KEY(status_id) REFERENCES status(id) ON UPDATE CASCADE
);