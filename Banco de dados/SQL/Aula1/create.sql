Create database TV_FILMES;
use TV_FILMES;

create table tipo(
id_tipo int not null auto_increment primary key,
nome varchar(30)
);

create table contatos(
id_contato int not null auto_increment primary key,
nome varchar(30),
email varchar(30),
senha varchar(30)
);

create table pais(
id_pais int not null auto_increment primary key,
nome varchar(30)
);

create table ator(
id_ator int not null auto_increment primary key,
nome varchar(30),
fk_id_pais int not null,
foreign key (fk_id_pais) references pais (id_pais) on update cascade on delete restrict
);

create table programatv(
id_programa int not null auto_increment primary key,
titulo varchar(30),
ano year,
fk_id_tipo int not null,
foreign key (fk_id_tipo) references tipo (id_tipo) on update cascade on delete restrict
);

create table genero(
id_genero int not null auto_increment primary key,
nome varchar(30)
);

create table plataforma(
id_plataforma int not null auto_increment primary key,
nome varchar(30)
);

CREATE TABLE prog_informacoes (
fk_id_programa INT NOT NULL,
id_prog_inf INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
titulo_original VARCHAR(30),
sinopse TEXT
);

create table prog_elenco(
fk_id_programa int not null,
fk_id_ator int not null,
chk_ator boolean,
chk_diretor boolean,
primary key (fk_id_programa, fk_id_ator),
foreign key (fk_id_programa) references programatv (id_programa) on update cascade on delete cascade,
foreign key (fk_id_ator) references ator (id_ator) on update cascade on delete cascade
);

create table disponibilidade(
fk_id_programa int not null,
fk_id_plataforma int not null,
primary key (fk_id_programa, fk_id_plataforma),
foreign key (fk_id_programa) references programatv (id_programa) on update cascade on delete cascade,
foreign key (fk_id_plataforma) references plataforma (id_plataforma) on update cascade on delete cascade
);

create table prog_genero(
id_programa int not null auto_increment primary key,
id_genero int,
foreign key (id_programa) references programatv (id),
foreign key (id_genero) references genero (id),
primary key (id_programa, id_genero)
);

create table avaliacao(
fk_id_contato int not null,
fk_id_programa int not null,
nota float,
primary key (fk_id_contato, fk_id_programa),
foreign key (fk_id_programa) references programatv (id_programa) on update cascade on delete cascade,
foreign key (fk_id_contato) references contato (id_contato) on update cascade on delete cascade
);