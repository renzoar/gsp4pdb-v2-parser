CREATE TABLE protein (
    id character varying(100) NOT NULL,
    title text,
    classification character varying(100),
    organism character varying(200),
    dep_date character varying(100),
    technique character varying(100),
    mod_date character varying(100)
);

CREATE TABLE chain (
    id character varying(100) NOT NULL,
    protein_id character varying(100),
    seqres text,
    num_het integer,
    num_amino integer
);


CREATE TABLE aminoacid (
    id character varying(100) NOT NULL,
    chain_id character varying(100),
    symbol character varying(100),
    protein_id character varying(100),
    next_amino character varying(100)
);


CREATE TABLE aminoacidstandard (
    name character varying(100) NOT NULL,
    symbol character varying(100) NOT NULL,
    classification character varying(100),
    abbreviation character varying(100) NOT NULL,
    class smallint
);

CREATE TABLE het (
    id character varying(100) NOT NULL,
    chain_id character varying(100),
    symbol character varying(100) NOT NULL,
    protein_id character varying(100),
    num_atom integer
);

CREATE TABLE atomamino (
    id character varying(100) NOT NULL,
    symbol character varying(100),
    number integer,
    x real,
    y real,
    z real,
    element character varying(100),
    amino_id character varying(100)
);

CREATE TABLE atomhet (
    id character varying(100) NOT NULL,
    symbol character varying(100),
    number integer,
    x real,
    y real,
    z real,
    element character varying(100),
    het_id character varying(100)
);


CREATE TABLE distance_amino_amino (
    amino1_id character varying(100) NOT NULL,
    amino1_symbol character varying(100) NOT NULL,
    amino1_class smallint NOT NULL,
    amino2_id character varying(100) NOT NULL,
    amino2_symbol character varying(100) NOT NULL,
    amino2_class smallint NOT NULL,
    min real NOT NULL,
    max real NOT NULL
);

CREATE TABLE distanceatomaminoatomhet (
    atom_amino_id character varying(100) NOT NULL,
    atom_het_id character varying(100) NOT NULL,
    distance real
);


CREATE TABLE distanceaminoamino (
    amino_id_1 character varying(100) NOT NULL,
    amino_id_2 character varying(100) NOT NULL,
    distance real
);

CREATE TABLE distanceaminohet (
    amino_id character varying(100) NOT NULL,
    het_id character varying(100) NOT NULL,
    distance real
);


CREATE TABLE distanceatomaminoatomamino (
    atom_amino_id_1 character varying(100) NOT NULL,
    atom_amino_id_2 character varying(100) NOT NULL,
    distance real
);



