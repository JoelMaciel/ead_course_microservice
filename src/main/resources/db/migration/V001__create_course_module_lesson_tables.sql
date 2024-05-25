CREATE TABLE COURSES (
    course_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(60) NOT NULL,
    description VARCHAR(200)NOT NULL,
    image_url VARCHAR(200),
    creation_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    course_status  VARCHAR(50) NOT NULL,
    course_level VARCHAR(50) NOT NULL,
    user_instructor UUID NOT NULL
);

CREATE TABLE MODULES (
    module_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(60) NOT NULL,
    description VARCHAR(200) NOT NULL,
    creation_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    course_id UUID NOT NULL,
    CONSTRAINT fk_course
      FOREIGN KEY(course_id)
	  REFERENCES COURSES(course_id)
);

CREATE TABLE LESSONS (
    lesson_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(60) NOT NULL,
    description VARCHAR(200) NOT NULL,
    creation_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    module_id UUID NOT NULL,
    CONSTRAINT fk_module
      FOREIGN KEY(module_id)
	  REFERENCES MODULES(module_id)
);
