
DELETE FROM COURSES_USERS;
DELETE FROM COURSES;

INSERT INTO courses (
    course_id,
    name,
    description,
    course_status,
    course_level,
    user_instructor,
    image_url,
    creation_date,
    update_date
) VALUES (
    '347a5bca-4734-4d1a-ad49-865dfa75b418',
    'Java',
    'AWS',
    'INPROGRESS',
    'BEGINNER',
    '3106c73c-5142-480b-8344-388610678971',
    'https://course.com/image.jpg',
    '2024-05-29 14:52:13',
    '2024-05-29 14:52:13'
);



insert into COURSES_USERS (id, course_id, user_id) values ('825922ca-1c75-43af-8600-0d42341cf449', '347a5bca-4734-4d1a-ad49-865dfa75b418', '5a96aa84-1f15-4333-ba60-54d99a3faccb')