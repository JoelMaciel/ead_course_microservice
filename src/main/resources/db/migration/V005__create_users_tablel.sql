CREATE TABLE USERS (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    user_status VARCHAR(50) NOT NULL,
    user_type VARCHAR(50) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    image_url TEXT
);