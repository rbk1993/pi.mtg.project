-- phpMyAdmin SQL Dump
-- version 4.4.3
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Sam 06 Juin 2015 à 18:18
-- Version du serveur :  5.6.24
-- Version de PHP :  5.6.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `my.tour.guide`
--

-- --------------------------------------------------------

--
-- Structure de la table `Administrateur_Ville`
--

CREATE TABLE IF NOT EXISTS `Administrateur_Ville` (
  `id_admin` int(11) NOT NULL,
  `ville_associée` varchar(255) NOT NULL,
  `pseudo` varchar(255) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `Administrateur_Ville`
--

INSERT INTO `Administrateur_Ville` (`id_admin`, `ville_associée`, `pseudo`, `mot_de_passe`) VALUES
(1, 'Lyon', 'admin', 'admin');

-- --------------------------------------------------------

--
-- Structure de la table `Groupe`
--

CREATE TABLE IF NOT EXISTS `Groupe` (
  `group_id` int(255) NOT NULL,
  `id_administrateur` int(255) NOT NULL,
  `duree_parcours_heures` int(255) NOT NULL,
  `duree_parcours_minutes` int(255) NOT NULL,
  `date` date NOT NULL,
  `horaire` time(4) NOT NULL,
  `nombre_membres_max` int(255) NOT NULL,
  `id_lieu_rencontre` int(255) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `Groupe`
--

INSERT INTO `Groupe` (`group_id`, `id_administrateur`, `duree_parcours_heures`, `duree_parcours_minutes`, `date`, `horaire`, `nombre_membres_max`, `id_lieu_rencontre`) VALUES
(1, 1, 2, 30, '2015-07-18', '13:20:00.0000', 10, 1),
(2, 2, 3, 15, '2015-07-20', '12:00:00.0000', 5, 5),
(3, 3, 4, 30, '2015-08-01', '16:00:00.0000', 15, 4),
(109, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5),
(110, 1, 5, 10, '2015-10-09', '20:00:00.0000', 20, 1),
(111, 1, 5, 10, '2016-10-09', '20:00:00.0000', 20, 1),
(112, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5),
(113, 1, 5, 10, '2015-10-09', '20:00:00.0000', 20, 1),
(114, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5),
(115, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5),
(116, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5),
(117, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5),
(118, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5),
(119, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5),
(120, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5),
(121, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5),
(122, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5),
(123, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5),
(124, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5),
(125, 99, 3, 30, '2015-09-01', '19:22:00.0000', 50, 5);

-- --------------------------------------------------------

--
-- Structure de la table `Lieu`
--

CREATE TABLE IF NOT EXISTS `Lieu` (
  `id_lieu` int(255) NOT NULL,
  `adresse_exacte` varchar(255) CHARACTER SET utf8 NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `description` text CHARACTER SET utf8 NOT NULL,
  `ville_associée` varchar(255) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `Lieu`
--

INSERT INTO `Lieu` (`id_lieu`, `adresse_exacte`, `latitude`, `longitude`, `description`, `ville_associée`) VALUES
(1, '', 45.767, 4.833, 'test lieu 1 (lyon)', 'Lyon'),
(2, '', 45.183, 5.717, 'test lieu 2 (grenoble)', 'Lyon'),
(3, '', 43.6, 3.883, 'test lieu 3 (montpellier)', 'Lyon'),
(4, '', 48.583, 7.75, 'test lieu 4 (strasbourg)', 'Lyon'),
(5, '', 45.433, 4.383, 'test lieu 5 (st-etienne)', 'Lyon'),
(6, '', 44.837789, -0.579179, 'test lieu 6 (Bordeaux)', 'Lyon'),
(7, '', 48.390394, -4.486076, 'test lieu 7 (brest)', 'Lyon'),
(8, '', 50.62925, 3.057256, 'test lieu 8 (lille)', 'Lyon'),
(9, '', 48.856614, 2.3522219, 'test lieu 9 (paris)', 'Lyon'),
(10, '', 43.7384176, 7.42461579, 'test lieu 10 (monaco)', 'Lyon');

-- --------------------------------------------------------

--
-- Structure de la table `Lieux_Groupes`
--

CREATE TABLE IF NOT EXISTS `Lieux_Groupes` (
  `id_lieu` int(255) NOT NULL,
  `group_id` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Lieux_Themes`
--

CREATE TABLE IF NOT EXISTS `Lieux_Themes` (
  `id_lieu` int(255) NOT NULL,
  `id_theme` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `Lieux_Themes`
--

INSERT INTO `Lieux_Themes` (`id_lieu`, `id_theme`) VALUES
(1, 1),
(2, 3),
(3, 4),
(4, 5),
(5, 1),
(6, 3),
(7, 4),
(8, 5),
(9, 5),
(10, 5);

-- --------------------------------------------------------

--
-- Structure de la table `Membres_Groupes`
--

CREATE TABLE IF NOT EXISTS `Membres_Groupes` (
  `user_id` int(255) NOT NULL,
  `group_id` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `Membres_Groupes`
--

INSERT INTO `Membres_Groupes` (`user_id`, `group_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(10, 1),
(11, 2),
(12, 3),
(13, 1),
(14, 2),
(15, 3),
(16, 1),
(17, 2),
(18, 3),
(19, 1),
(10, 2),
(11, 3),
(12, 1),
(13, 2),
(99, 107),
(99, 108),
(99, 109),
(1, 1),
(1, 1),
(99, 112),
(1, 113),
(99, 114),
(99, 115),
(99, 116),
(99, 117),
(99, 118),
(99, 119),
(99, 120),
(99, 121),
(99, 122),
(99, 123),
(99, 124),
(99, 125);

-- --------------------------------------------------------

--
-- Structure de la table `Membres_Groupes_A_Approuver`
--

CREATE TABLE IF NOT EXISTS `Membres_Groupes_A_Approuver` (
  `user_id` int(255) NOT NULL,
  `group_id` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Theme`
--

CREATE TABLE IF NOT EXISTS `Theme` (
  `id_theme` int(11) NOT NULL,
  `ville_associée` varchar(255) NOT NULL,
  `nom_theme` varchar(255) NOT NULL,
  `photo_theme` varchar(255) NOT NULL,
  `description` longtext NOT NULL,
  `available` tinyint(1) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `Theme`
--

INSERT INTO `Theme` (`id_theme`, `ville_associée`, `nom_theme`, `photo_theme`, `description`, `available`) VALUES
(1, 'Lyon', 'culture', '1.jpeg', 'Regroupe les différents lieux et événements culturels à Lyon (Musées, Sites Historiques, Expositions, Monuments Historiques ...).', 1),
(3, 'Lyon', 'nature', '3.jpeg', 'Regroupe les lieux et événements destinés aux amoureux de la nature (Parcs, Restaurants Bio, Produits du terroir, ...).', 1),
(4, 'Lyon', 'gastronomie', '4.jpeg', 'Regroupe les différents lieux et événements destinés aux amoureux de la gastronomie (Restaurants, Brasseries, Dégustations ...).', 1),
(5, 'Lyon', 'amusement', '5.jpeg', 'Regroupe l''ensemble des lieux et événéments où vous pouvez vous amuser (Parcs à thème, Kartings, Bowlings, Patinoires, Pubs avec Billard...)', 0);

-- --------------------------------------------------------

--
-- Structure de la table `Utilisateur`
--

CREATE TABLE IF NOT EXISTS `Utilisateur` (
  `user_id` int(255) NOT NULL,
  `prenom` varchar(255) CHARACTER SET utf8 NOT NULL,
  `nom` varchar(255) CHARACTER SET utf8 NOT NULL,
  `date_naissance` date NOT NULL,
  `login` varchar(255) CHARACTER SET utf8 NOT NULL,
  `mail` varchar(255) CHARACTER SET utf8 NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 NOT NULL,
  `telephone` bigint(20) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1 COMMENT='Table enregistrant les utilisateurs de l''application';

--
-- Contenu de la table `Utilisateur`
--

INSERT INTO `Utilisateur` (`user_id`, `prenom`, `nom`, `date_naissance`, `login`, `mail`, `password`, `telephone`) VALUES
(1, 'Reda', 'Benkirane', '1993-05-06', 'rbk1993', 'reda.benkirane@hotmail.fr', '12345', 0),
(2, 'root', 'root', '2015-06-02', 'root', 'root@root.com', 'root', 0),
(3, 'test1', 'test1', '2015-06-02', 'test1', 'test1@test.com', 'test1', 0),
(10, 'test0', 'test0', '2015-06-02', 'test0', 'test0@test.com', 'test0', 0),
(11, 'test2', 'test2', '2015-06-02', 'test2', 'test2@test.com', 'test2', 0),
(12, 'test3', 'test3', '2015-06-02', 'test3', 'test3@test.com', 'test3', 0),
(13, 'test4', 'test4', '2015-06-02', 'test4', 'test4@test.com', 'test4', 0),
(14, 'test5', 'test5', '2015-06-02', 'test5', 'test5@test.com', 'test5', 0),
(15, 'test6', 'test6', '2015-06-02', 'test6', 'test6@test.com', 'test6', 0),
(16, 'test7', 'test7', '2015-06-02', 'test7', 'test7@test.com', 'test7', 0),
(17, 'test8', 'test8', '2015-06-02', 'test8', 'test8@test.com', 'test8', 0),
(18, 'test9', 'test9', '2015-06-02', 'test9', 'test9@test.com', 'test9', 0),
(19, 'test10', 'test10', '2015-06-02', 'test10', 'test10@test.com', 'test10', 0),
(20, 'blabla', 'blabla', '1991-05-02', 'blabla', 'blabla@blabla.fr', 'blabla', 10101010101);

--
-- Index pour les tables exportées
--

--
-- Index pour la table `Administrateur_Ville`
--
ALTER TABLE `Administrateur_Ville`
  ADD PRIMARY KEY (`id_admin`);

--
-- Index pour la table `Groupe`
--
ALTER TABLE `Groupe`
  ADD PRIMARY KEY (`group_id`);

--
-- Index pour la table `Lieu`
--
ALTER TABLE `Lieu`
  ADD PRIMARY KEY (`id_lieu`);

--
-- Index pour la table `Theme`
--
ALTER TABLE `Theme`
  ADD PRIMARY KEY (`id_theme`),
  ADD UNIQUE KEY `nom_theme` (`nom_theme`);

--
-- Index pour la table `Utilisateur`
--
ALTER TABLE `Utilisateur`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `login` (`login`),
  ADD UNIQUE KEY `mail` (`mail`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `Administrateur_Ville`
--
ALTER TABLE `Administrateur_Ville`
  MODIFY `id_admin` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT pour la table `Groupe`
--
ALTER TABLE `Groupe`
  MODIFY `group_id` int(255) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=126;
--
-- AUTO_INCREMENT pour la table `Lieu`
--
ALTER TABLE `Lieu`
  MODIFY `id_lieu` int(255) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT pour la table `Theme`
--
ALTER TABLE `Theme`
  MODIFY `id_theme` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT pour la table `Utilisateur`
--
ALTER TABLE `Utilisateur`
  MODIFY `user_id` int(255) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=21;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
