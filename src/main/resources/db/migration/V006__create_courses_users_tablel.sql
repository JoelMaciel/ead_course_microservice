CREATE TABLE COURSES_USERS (
    course_id UUID NOT NULL,
    user_id UUID NOT NULL,
    PRIMARY KEY (course_id, user_id),
    CONSTRAINT fk_course
        FOREIGN KEY (course_id)
        REFERENCES COURSES (course_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES USERS (user_id)
        ON DELETE CASCADE
);