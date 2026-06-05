CREATE TABLE IF NOT EXISTS profiles (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT,

    name VARCHAR(100) NOT NULL,

    email VARCHAR(120) NOT NULL UNIQUE,

    image_url VARCHAR(500),

    public_id VARCHAR(200),

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_profiles_email
    ON profiles(email);

CREATE INDEX IF NOT EXISTS idx_profiles_created_at
    ON profiles(created_at);