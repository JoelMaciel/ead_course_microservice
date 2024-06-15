
DELETE FROM courses;
DELETE FROM users;

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


INSERT INTO USERS (user_id, email, full_name, user_status, user_type, cpf, image_url)
VALUES
('5a96aa84-1f15-4333-ba60-54d99a3faccb', 'john@example.com', 'John Doe', 'ACTIVE', 'ADMIN', '12345678901', 'https://example.com/image1.jpg'),
('8c6b464b-03f4-4a2d-ac59-19584ee09d4f', 'jane@example.com', 'Jane Smith', 'BLOCKED', 'STUDENT', '23456789012', 'https://example.com/image2.jpg'),
('79ff1531-1d28-44fc-b20a-ccb4cbbe71d7', 'alice@example.com', 'Alice Johnson', 'ACTIVE', 'INSTRUCTOR', '34567890123', 'https://example.com/image3.jpg');
