-- Script de inicialização do banco MySQL
CREATE DATABASE IF NOT EXISTS revenda_veiculos;
USE revenda_veiculos;

-- Criar usuário 'app' com senha 'app123'
CREATE USER IF NOT EXISTS 'app'@'%' IDENTIFIED BY 'app123';

-- Conceder todas as permissões para o usuário 'app' no banco 'revenda_veiculos'
GRANT ALL PRIVILEGES ON revenda_veiculos.* TO 'app'@'%';

-- Aplicar as alterações
FLUSH PRIVILEGES;

-- Verificar se o banco foi criado
SHOW DATABASES;

-- Criar tabelas se não existirem (o JPA criará, mas é bom ter como backup)
-- As tabelas serão criadas automaticamente pelo JPA com ddl-auto=update

-- Inserir dados de exemplo (opcional)
-- INSERT INTO veiculos (marca, modelo, ano, cor, preco, status, data_cadastro)
-- VALUES ('Toyota', 'Corolla', 2023, 'Branco', 85000.00, 'DISPONIVEL', NOW());
