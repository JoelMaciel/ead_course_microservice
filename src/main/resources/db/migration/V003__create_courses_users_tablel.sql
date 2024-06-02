CREATE TABLE COURSES_USERS (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() ,
    course_id UUID NOT NULL ,
    user_id UUID NOT NULL ,
    CONSTRAINT fk_course
        FOREIGN KEY(course_id)
        REFERENCES COURSES(course_id)
);