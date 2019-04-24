/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.banque.model;

import com.douwe.banque.data.Traitement;
import java.util.List;

/**
 *
 * @author Maurice PENKA
 */
public interface IServiceOperation {
    
    /**
     * methode permetant de renvoyer la liste des operations en fonction de la
     * requete pris en parametre
     *
     * @param requete
     * @return la liste des operations
     */
    public List<Traitement> findAllOperation(String requete);
    
    
    /**
     * methode permettant d'effectuer un transfert de montant d'un compte source
     * vers un compte destination et retourne 1 si tous se passe bien 2 si le
     * compte destination n'existe pas 3 si le compte source ne dispose pas un
     * solde suffisant 4 si le montant pris en entré n'est pas un nombre.
     *
     * @param source : compte source
     * @param destination : compte destination
     * @param montant : montant à transferé
     * @return 1 si tous se passe bien 2 si le compte destination n'existe pas 3
     * si le compte source ne dispose pas un solde suffisant 4 si le montant
     * pris en entré n'est pas
     */
    public int transfert(String source, String destination, String montant);
    
    
    /**
     * methode permettant d'enregistrer le operations de 
     * suppression de comptes bancaire
     * 
     * @param numeroCompte: numero de compte
     * @param id identifiant du compte
     * @return 1 si tous se passe bien 2 si Impossible de supprimer ce compte
     * et 3 si Impossible de fermer la ressource
     */
    public int saveTraitementDeleteCompte(String numeroCompte, int id);
}
