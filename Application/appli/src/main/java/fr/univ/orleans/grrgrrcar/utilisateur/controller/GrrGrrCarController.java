package fr.univ.orleans.grrgrrcar.utilisateur.controller;

import fr.univ.orleans.grrgrrcar.utilisateur.gestionToken.DemandeToken;
import fr.univ.orleans.grrgrrcar.utilisateur.gestionToken.proxyservice.ProxyService;
import fr.univ.orleans.grrgrrcar.utilisateur.gestionToken.proxyservice.exceptions.NoAuthenticationServerIsKnownException;
import fr.univ.orleans.grrgrrcar.utilisateur.gestionToken.proxyservice.exceptions.NoServerFoundException;
import fr.univ.orleans.grrgrrcar.utilisateur.gestionToken.proxyservice.exceptions.UnknownUserException;
import io.jsonwebtoken.*;
import modele.Personne;
import modele.Trajet;
import modele.Vote;
import modele.exceptions.authentification.*;
import modele.exceptions.gestionAppli.*;
import modele.facade.GestionGrrGrrCar;
import modele.facade.GestionGrrGrrCarImpl;
import modele.reservation.Reservation;
import modele.reservation.types.ReservationValidee;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;


@RestController
@RequestMapping("/grrgrrcar")
public class GrrGrrCarController {

    private static final long EXPIRATION_TIME = 1000 * 120L;
    private static final String SECRET_KEY = "CANARD" ;
    private static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";

    private static GestionGrrGrrCar gestionFacade;

    public DemandeToken demandeToken;
    public String token;
    public String authenticationUri;


    public GrrGrrCarController() {
       gestionFacade = new GestionGrrGrrCarImpl();
    }

    @RequestMapping(value = "/trajet", method = RequestMethod.GET)
    public Collection<Trajet> getAllTrajets()
    {
        return gestionFacade.getLesTrajets();
    }


    @RequestMapping(value = "/trajet",method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Trajet> createTrajet(@RequestBody Trajet trajet) throws ParseException {
        gestionFacade.creerTrajet(trajet.getDepart() ,trajet.getArrivee() ,trajet.getDateDepart() ,trajet.getNbPlaces(),trajet.getTarif(),trajet.getCreateur());
        return new ResponseEntity<Trajet>(trajet ,HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.DELETE)
    public Collection<Trajet> deleteAllTrajets()
    {
        return gestionFacade.getLesTrajets();
    }

    @RequestMapping(value = "/inscription", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Personne> inscription(@RequestBody Personne paramPersonne) throws PersonneIntrouvableException {
        try {
            //Vérification des champs, ils ne peuvent pas être vide, ils sont obligatoire
            if(paramPersonne.getPseudo().isEmpty() || paramPersonne.getMotDePasse().isEmpty()){
                //Si ils sont vide alors on retourne NOT_ACCEPTABLE --> 406
                return new ResponseEntity<Personne>(paramPersonne, HttpStatus.NOT_ACCEPTABLE);
            }else{
                //Sinon, les champs ne sont pas vide, alors on procède à l'inscription, on retourne CREATED --> 201
                gestionFacade.inscription(paramPersonne.getPseudo(), paramPersonne.getMotDePasse());
                return new ResponseEntity<Personne>(paramPersonne, HttpStatus.CREATED);
            }
        } catch (PseudoDejaPrisException e) {
            //Exception dans le cas ou le login est déjà pris, on retourne BAD_REQUEST --> 400
            return new ResponseEntity<Personne>(paramPersonne, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/connexion", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Personne> connexion(@RequestBody Personne paramPersonne) {
        // Vérifier si le login/password sont OK
        try {
            gestionFacade.connexion(paramPersonne.getPseudo(), paramPersonne.getMotDePasse());
            // login réussi
            //Générer le token
            Claims claims = Jwts.claims().setSubject(paramPersonne.getPseudo());
            // On stocke toutes les infos de l'utilisateur
            claims.put("pseudo", paramPersonne.getPseudo());
            // https://jwt.io/ pour vérifier
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                    .compact();
            //HS512 mais possiblement plein d'autres
            // Le compact va encoder une chaine de caracteres en base 64

            // Renvoyer le token
            return ResponseEntity.ok().header(AUTHORIZATION,TOKEN_PREFIX + token).build();
        } catch (MotDePasseIncorrectException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DejaConnecteException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (PersonneIntrouvableException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/checkToken")
    public ResponseEntity<String> checkToken(
            @RequestHeader(value = AUTHORIZATION, required = true) String token)
    {
        String tokenToCheck = token.replace(TOKEN_PREFIX, "");
        Jws<Claims> jwsClaims = null;

        try{
            jwsClaims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(tokenToCheck);
        }catch (ExpiredJwtException e){ // check jwt exceptions
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        }catch (SignatureException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String login = jwsClaims.getBody().getSubject();

        // Pas terrible
        return ResponseEntity.ok(login);
    }

    public void demandeToken(String login, String password) {
        try {
            token = ProxyService.getToken(authenticationUri,login,password);
            //goToMenu();
        } catch (NoAuthenticationServerIsKnownException e) {
            demandeToken.help();
            //goToMenu();
        } catch (NoServerFoundException e) {
            demandeToken.erreurServeur();
            //goToMenu();
        } catch (UnknownUserException e) {
            demandeToken.erreur();
            //goToMenu();
        } catch (ResourceAccessException e) {
            demandeToken.erreurServeur();
            //goToMenu();
        }
    }

    @RequestMapping(value = "/deconnexion", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Personne> deconnexion(@RequestBody Personne paramPersonne) throws PersonneIntrouvableException {
        try {
            gestionFacade.deconnexion(paramPersonne.getPseudo());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (DejaDeconnecteException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/{pseudo}",method = RequestMethod.GET)
    public Personne getInformationPersonne(@PathVariable("pseudo") String pseudo) throws PersonneIntrouvableException {
        Personne maPersonne = gestionFacade.getPersonneByPseudo(pseudo);
        return maPersonne;
    }

    @RequestMapping(value = "/{pseudo}",method = RequestMethod.PUT,consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Personne> enregistrementInformation(
            @PathVariable("pseudo") String pseudo,
            @RequestParam("dateNaiss") String dateNaiss,
            @RequestBody Personne paramPersonne) {
        try {

            String newHeureDep = dateNaiss.substring(1,11);

            Date datePersonne = new Date(newHeureDep);

            gestionFacade.modifierPersonne(
                    gestionFacade.getPersonneByPseudo(pseudo).getId(),
                    paramPersonne.getNom(),
                    paramPersonne.getPrenom(),
                    paramPersonne.getAdresse(),
                    paramPersonne.getVille(),
                    paramPersonne.getCodePostal(),
                    paramPersonne.getEmail(),
                    datePersonne);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (PersonneIntrouvableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /*
    ----------------------------------------------------------------
    ----------------------------------------------------------------
    ----------------------------------------------------------------
     */

    @RequestMapping(value = "/trajet/{idTrajet}", method = RequestMethod.GET)
    public Trajet getTrajetById(@PathVariable("idTrajet") int idTrajet) throws TrajetIntrouvableException {
        Trajet unTrajet = gestionFacade.getTrajetById(idTrajet);
        return unTrajet;
    }

    @RequestMapping(value = "/trajet/{idTrajet}/votes", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Vote> createVoteByPassanger(@RequestBody Vote unVote, @PathVariable("idTrajet") int idTrajet, @RequestParam("pseudoTitulaire") String pseudoTitulaire) {
        try {
            if(gestionFacade.getTrajetById(idTrajet).getCreateur().equals(unVote.getPseudoVotant())){
                gestionFacade.conducteurNoterPassager(idTrajet, unVote.getPseudoVotant(), pseudoTitulaire, unVote.getNote(), unVote.getCommentaire());
                return new ResponseEntity<Vote>(unVote, HttpStatus.CREATED);
            }else{
                gestionFacade.passagerNoterTrajet(idTrajet, unVote.getPseudoVotant(), unVote.getNote(), unVote.getCommentaire());
                return new ResponseEntity<Vote>(unVote, HttpStatus.CREATED);
            }
        } catch (TrajetIntrouvableException e) {
            return new ResponseEntity<Vote>(unVote, HttpStatus.NOT_FOUND);
        } catch (PasPassagerTrajetException e) {
            return new ResponseEntity<Vote>(unVote, HttpStatus.NOT_ACCEPTABLE);
        } catch (PersonneIntrouvableException e) {
            return new ResponseEntity<Vote>(unVote, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/trajet/{idTrajet}/validation", method = RequestMethod.PUT)
    public ResponseEntity<Trajet> validerTrajet(@PathVariable int idTrajet, @RequestParam("pseudo")  String pseudo) throws TrajetIntrouvableException {
        Trajet trajet = gestionFacade.getTrajetById(idTrajet);
        try {
            gestionFacade.validerFinTrajet(idTrajet, pseudo);
            return new ResponseEntity<Trajet>(trajet, HttpStatus.ACCEPTED);
        } catch (TrajetIntrouvableException e) {
            return new ResponseEntity<Trajet>(trajet, HttpStatus.NOT_FOUND);
        } catch (ConducteurValideFinTrajetException e) {
            return new ResponseEntity<Trajet>(trajet, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "/rechercheTrajet", method = RequestMethod.GET)
    public Collection<Trajet> rechercheTrajet(
            @RequestParam("villeDepart") String villeDepart,
            @RequestParam("villeArrivee") String villeArrivee,
            @RequestParam("dateDepart") String dateDepart)  {
        dateDepart = dateDepart.replace(";"," ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm" ,Locale.FRANCE);
        Collection<Trajet> mesTrajetsFiltres = new ArrayList<>();
        try {
            Date date = sdf.parse(dateDepart);
            mesTrajetsFiltres=gestionFacade.rechercheTrajet(villeArrivee,villeDepart,date);
        } catch (ParseException e) {

        }
        return mesTrajetsFiltres;
    }


    @RequestMapping(value = "/trajet/{idTrajet}/reservation", method = RequestMethod.POST)
    public ResponseEntity<Reservation> createReservation(
            @PathVariable("idTrajet")int idTrajet,
            @RequestParam("pseudoReservant")String pseudo) {
        try {
            gestionFacade.nouvelleDemande(idTrajet,pseudo);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (TrajetIntrouvableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (TrajetCompletException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (TrajetDejaDemandeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (TrajetNonDemandableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(value = "/trajet/{idTrajet}/reservation/{idReservation}", method = RequestMethod.PUT)
    public ResponseEntity<Reservation> modifierReservation(
            @PathVariable("idTrajet")int idTrajet,
            @PathVariable("idReservation")int idReservation,
            @RequestParam("etat")int etat) throws ReservationIntrouvableException, TrajetIntrouvableException, TrajetCompletException {
        Reservation reservation =gestionFacade.getReservationById(idReservation);
        switch (etat){
            case 1 :
                gestionFacade.validerDemande(idTrajet,reservation.getPseudoReservant());
                return ResponseEntity.status(HttpStatus.OK).build();
            case 2 :
                gestionFacade.refuserDemande(idTrajet,reservation.getPseudoReservant());
                return ResponseEntity.status(HttpStatus.OK).build();
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

