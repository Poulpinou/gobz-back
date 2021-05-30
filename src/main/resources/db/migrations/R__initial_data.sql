-- Create Users
insert into users (id, name, email, password, email_verified, image_url, provider, provider_id, created_at)
values (1, 'User #1', 'user1@test.com', '${defaultPassword}', true,
        'https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png',
        'local', null, NOW()),
       (2, 'User #2', 'user2@test.com', '${defaultPassword}', true,
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSaijpbXwpLwhznK49LfyuN6hvcthp_1MyXKA&usqp=CAU',
        'local', null, NOW()),
       (3, 'User #3', 'user3@test.com', '${defaultPassword}', true,
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSkVbYlOjsWmZlx2xIh7aO3Xcto3FHTYOkQkA&usqp=CAU',
        'local', null, NOW()),
       (4, 'User #4', 'user3@test.com', '${defaultPassword}', true,
        null,
        'local', null, NOW())
on duplicate key update id    = id,
                        email = email;

-- Create projects
insert into projects (id, name, description, shared, created_at, created_by)
values (1, 'Test Project #1', 'A generated project for testing purpose', true, NOW(), 1),
       (2, 'Test Project #2', 'An empty project generated project for testing purpose', true, NOW(), 1),
       (3, 'Test Project #3', 'A generated project for testing purpose with no user inside', true, NOW(), 1)
on duplicate key update id = id;

-- Create chapters
insert into chapters (id, name, description, project_id, created_at, created_by)
values (1, 'Chapter #1', 'A test chapter', 1, NOW(), 1),
       (2, 'Chapter #2', 'An empty test chapter', 1, NOW(), 1),
       (3, 'Chapter #1', 'A test chapter', 3, NOW(), 1),
       (4, 'Chapter #2', 'An empty test chapter', 3, NOW(), 1)
on duplicate key update id = id;

-- Create Steps
insert into steps (id, name, description, chapter_id, created_at, created_by)
values (1, 'Step #1', 'A test step', 1, NOW(), 1),
       (2, 'Step #2', 'An empty test step', 1, NOW(), 1),
       (3, 'Step #1', 'A test step', 3, NOW(), 1),
       (4, 'Step #2', 'An empty test step', 3, NOW(), 1)
on duplicate key update id = id;

-- Create tasks
insert into tasks (id, text, done, step_id, created_at, created_by)
values (1, 'Task #1', true, 1, NOW(), 1),
       (2, 'Task #2', false, 1, NOW(), 1),
       (3, 'Task #1', true, 3, NOW(), 1),
       (4, 'Task #2', false, 3, NOW(), 1)
on duplicate key update id = id;

-- Create membership
insert into project_members (id, user_id, project_id, role)
values
    -- User #1 owns every projects
    (1, 1, 1, 'OWNER'),
    (2, 1, 2, 'OWNER'),
    (3, 1, 3, 'OWNER'),

    -- User #2 can contribute to any project
    (4, 2, 1, 'CONTRIBUTOR'),
    (5, 2, 2, 'CONTRIBUTOR'),
    (6, 2, 3, 'CONTRIBUTOR'),

    -- User #3 can see every project
    (7, 3, 1, 'VIEWER'),
    (8, 3, 2, 'VIEWER'),
    (9, 3, 3, 'VIEWER')

    -- User #4 isn't part of any project
on duplicate key update id = id;
