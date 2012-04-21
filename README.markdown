# Expérimentation : Dashboard GitHub / Recherche de projets

> Application expérimentale permettant de rechercher des projets sur Github et d'en extraire des statistiques détaillées

![Screenshot of Gihub Dashboard](http://up.studio-dev.fr/_/capturedcran20120421225940.png)

## Socle technologique

### Server-side 

* PlayFramework 2 
* Scala
* Sindi (IOC Container)
* Spec2 (testing)

### Client-side

* Backbone
* Handlebars
* UnderscoreJS
* Lawnchair
* Selenium2

## Fonctionnalités

* Recherche de projets Github avec plusieurs critères : nom, auteur, language principal
* Statistiques sur l'activité d'un dépôt (Meilleurs contributeurs, activité jour par jour)
* Traitements asynchrones et cache client (localstorage) / server
* Résultats de recherche affichés en "infinite-scroll"
* API REST pour accéder aux informations suivantes :
  * Repository github : détail, liste des commits, des commiters, des watchers, des langauges
  * User github : détail, liste des languages, liste des repositories
  * Stats : Activité d'un repository, timeline
  * Géolocalisation des utilisateurs (via Yahoo Geocoder)

## Exécution

1. Installez PlayFramework 2.0 

2. Lancez simplement l'appli avec le serveur Play20 :

    $ play run

## Licence

Cette application est distribuée sous licence Creative Commons BY-NC-SA 3.0.

http://creativecommons.org/licenses/by-nc-sa/3.0/

-- Julien Lafont (http://www.studio-dev.fr)
