INSERT IGNORE INTO `roles` (`id`, `name`)
VALUES
	(1, 'ROLE_ADMIN'),
	(2, 'ROLE_COMPANY'),
	(3, 'ROLE_JOBSEEKER');

INSERT IGNORE INTO `users` (`id`, `name`, `username`, `password`, `email`)
VALUES
    (1, 'Test', 'TestName', '$2a$10$jN1DABY9UcjOc9LkC0619exAZ9.IClPnTb71hgrj3gmz6uobBM9Au', 'test@gmail.com');

INSERT IGNORE INTO `user_roles` (`user_id`, `role_id`)
VALUES
    (1, 1);
