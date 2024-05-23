import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

// Interfaces
interface DossierMedical {
    void ajouterConsultation(Consultation consultation);
    void ajouterTraitement(Traitement traitement);
    List<Consultation> getConsultations();
    List<Traitement> getTraitements();
}

interface GestionRendezVous {
    void prendreRendezVous(Patient patient, Date date);
    void annulerRendezVous(Date date);
    List<RendezVous> getRendezVous();
}

interface GestionFichesPatients {
    void ajouterFichePatient(FichePatient fichePatient);
    void ajouterObservationMedicale(ObservationMedicale observation, FichePatient fichePatient);
    List<FichePatient> getFichesPatients();
}

// Classes abstraites
abstract class Personne {
    protected String nom;
    protected String prenom;
    protected String telephone;

    public Personne(String nom, String prenom, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }
}

class Consultation {
    private Date date;
    private String resume;

    public Consultation(Date date, String resume) {
        this.date = date;
        this.resume = resume;
    }

    public Date getDate() {
        return date;
    }

    public String getResume() {
        return resume;
    }
}

class Traitement {
    private String medicament;
    private String dose;
    private int duree;

    public Traitement(String medicament, String dose, int duree) {
        this.medicament = medicament;
        this.dose = dose;
        this.duree = duree;
    }

    public String getMedicament() {
        return medicament;
    }

    public String getDose() {
        return dose;
    }

    public int getDuree() {
        return duree;
    }
}

class FichePatient implements DossierMedical {
    protected String nom;
    protected String prenom;
    protected String telephone;
    protected String antecedents;
    protected List<ObservationMedicale> observations;
    protected List<Consultation> consultations;
    protected List<Traitement> traitements;

    public FichePatient(String nom, String prenom, String telephone, String antecedents) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.antecedents = antecedents;
        this.observations = new ArrayList<>();
        this.consultations = new ArrayList<>();
        this.traitements = new ArrayList<>();
    }

    @Override
    public void ajouterConsultation(Consultation consultation) {
        consultations.add(consultation);
    }

    @Override
    public void ajouterTraitement(Traitement traitement) {
        traitements.add(traitement);
    }

    @Override
    public List<Consultation> getConsultations() {
        return consultations;
    }

    @Override
    public List<Traitement> getTraitements() {
        return traitements;
    }

    public void ajouterObservationMedicale(ObservationMedicale observation) {
        observations.add(observation);
    }

    public List<ObservationMedicale> getObservations() {
        return observations;
    }
}

// Classes concrètes
class ObservationMedicale {
    private String observations;

    public ObservationMedicale(String observations) {
        this.observations = observations;
    }

    public String getObservations() {
        return observations;
    }
}

class Patient extends Personne {
    public Patient(String nom, String prenom, String telephone) {
        super(nom, prenom, telephone);
    }
}

class Medecin extends Personne implements GestionRendezVous, GestionFichesPatients {
    private List<RendezVous> rendezVous;
    private List<FichePatient> fichesPatients;

    public Medecin(String nom, String prenom, String telephone) {
        super(nom, prenom, telephone);
        this.rendezVous = new ArrayList<>();
        this.fichesPatients = new ArrayList<>();
    }

    @Override
    public void prendreRendezVous(Patient patient, Date date) {
        rendezVous.add(new RendezVous(patient, date));
    }

    @Override
    public void annulerRendezVous(Date date) {
        rendezVous.removeIf(rdv -> rdv.getDate().equals(date));
    }

    @Override
    public List<RendezVous> getRendezVous() {
        return rendezVous;
    }

    @Override
    public void ajouterFichePatient(FichePatient fichePatient) {
        fichesPatients.add(fichePatient);
    }

    @Override
    public void ajouterObservationMedicale(ObservationMedicale observation, FichePatient fichePatient) {
        fichePatient.ajouterObservationMedicale(observation);
    }

    @Override
    public List<FichePatient> getFichesPatients() {
        return fichesPatients;
    }

    public FichePatient trouverFichePatient(String nom) {
        for (FichePatient fiche : fichesPatients) {
            if (fiche.nom.equals(nom)) {
                return fiche;
            }
        }
        return null;
    }
}

class Secretaire extends Personne implements GestionFichesPatients, GestionRendezVous {
    private List<FichePatient> fichesPatients;
    private List<RendezVous> rendezVous;

    public Secretaire(String nom, String prenom, String telephone) {
        super(nom, prenom, telephone);
        this.fichesPatients = new ArrayList<>();
        this.rendezVous = new ArrayList<>();
    }

    @Override
    public void ajouterFichePatient(FichePatient fichePatient) {
        fichesPatients.add(fichePatient);
    }

    @Override
    public void ajouterObservationMedicale(ObservationMedicale observation, FichePatient fichePatient) {
        fichePatient.ajouterObservationMedicale(observation);
    }

    @Override
    public List<FichePatient> getFichesPatients() {
        return fichesPatients;
    }

    public void afficherFichePatient(String nom) {
        for (FichePatient fiche : fichesPatients) {
            if (fiche.nom.equals(nom)) {
                System.out.println("Fiche pour " + fiche.nom + " " + fiche.prenom);
                System.out.println("  Antécédents: " + fiche.antecedents);
                System.out.println("  Observations:");
                for (ObservationMedicale obs : fiche.getObservations()) {
                    System.out.println("    - " + obs.getObservations());
                }
                System.out.println("  Consultations:");
                for (Consultation cons : fiche.getConsultations()) {
                    System.out.println("    - " + cons.getResume() + " le " + cons.getDate());
                }
                System.out.println("  Traitements:");
                for (Traitement traitement : fiche.getTraitements()) {
                    System.out.println("    - " + traitement.getMedicament() + ", " + traitement.getDose() + " pour " + traitement.getDuree() + " jours");
                }
                return;
            }
        }
        System.out.println("Aucun patient trouvé avec le nom : " + nom);
    }

    @Override
    public void prendreRendezVous(Patient patient, Date date) {
        rendezVous.add(new RendezVous(patient, date));
    }

    @Override
    public void annulerRendezVous(Date date) {
        rendezVous.removeIf(rdv -> rdv.getDate().equals(date));
    }

    @Override
    public List<RendezVous> getRendezVous() {
        return rendezVous;
    }
}

class RendezVous {
    private Patient patient;
    private Date date;

    public RendezVous(Patient patient, Date date) {
        this.patient = patient;
        this.date = date;
    }

    public Patient getPatient() {
        return patient;
    }

    public Date getDate() {
        return date;
    }
}

// Classe principale
public class Poo {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        Medecin docteur = new Medecin("BOUMAHRA", "Nada", "555123456");
        Secretaire secretaire = new Secretaire("HAMADOU", "Sarah", "555987654");

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");

        while (true) {
            System.out.println("Choisissez votre rôle : 1- Médecin, 2- Secrétaire, 0- Quitter");
            int role = scanner.nextInt();
            scanner.nextLine(); // consommer le retour à la ligne

            if (role == 1) {
                // Actions du médecin
                System.out.println("Choisissez : 1- Ajouter un patient, 2- Voir les rendez-vous, 3- Voir les fiches patients");
                int actionMedecin = scanner.nextInt();
                scanner.nextLine(); // consommer le retour à la ligne

                if (actionMedecin == 1) {
                    // Ajouter les informations du patient
                    System.out.print("Entrez le nom du patient : ");
                    String nomPatient = scanner.nextLine();
                    System.out.print("Entrez le prénom du patient : ");
                    String prenomPatient = scanner.nextLine();
                    System.out.print("Entrez le téléphone du patient : ");
                    String telephonePatient = scanner.nextLine();
                    System.out.print("Entrez les antécédents du patient : ");
                    String antecedents = scanner.nextLine();

                    Patient nouveauPatient = new Patient(nomPatient, prenomPatient, telephonePatient);
                    FichePatient nouvelleFiche = new FichePatient(nomPatient, prenomPatient, telephonePatient, antecedents);
                    docteur.ajouterFichePatient(nouvelleFiche);

                    System.out.print("Entrez l'observation médicale : ");
                    String observation = scanner.nextLine();
                    ObservationMedicale obsMed = new ObservationMedicale(observation);
                    nouvelleFiche.ajouterObservationMedicale(obsMed);

                    System.out.print("Entrez le résumé de la consultation : ");
                    String resumeConsultation = scanner.nextLine();
                    System.out.print("Entrez la date de la consultation (AAAA/MM/JJ) : ");
                    String dateConsultationStr = scanner.nextLine();
                    Date dateConsultation = formatDate.parse(dateConsultationStr);
                    Consultation consultation = new Consultation(dateConsultation, resumeConsultation);
                    nouvelleFiche.ajouterConsultation(consultation);

                    System.out.print("Entrez le médicament du traitement : ");
                    String medicament = scanner.nextLine();
                    System.out.print("Entrez la dose du traitement : ");
                    String dose = scanner.nextLine();
                    System.out.print("Entrez la durée du traitement (jours) : ");
                    int duree = scanner.nextInt();
                    scanner.nextLine(); // consommer le retour à la ligne
                    Traitement traitement = new Traitement(medicament, dose, duree);
                    nouvelleFiche.ajouterTraitement(traitement);

                    System.out.println("Informations du patient ajoutées avec succès.");
                } else if (actionMedecin == 2) {
                    // Voir les rendez-vous
                    List<RendezVous> rendezVous = docteur.getRendezVous();
                    if (rendezVous.isEmpty()) {
                        System.out.println("Aucun rendez-vous planifié.");
                    } else {
                        System.out.println("Rendez-vous :");
                        for (RendezVous rdv : rendezVous) {
                            System.out.println("Patient : " + rdv.getPatient().nom + " " + rdv.getPatient().prenom + ", Date : " + rdv.getDate());
                        }
                    }
                }else if (actionMedecin == 3) {
                    // Voir les fiches patients
                    if (docteur.getFichesPatients().isEmpty()) {
                        System.out.println("Aucun patient dans la liste.");
                    } else {
                        System.out.print("Entrez le nom du patient pour afficher la fiche : ");
                        String nomPatient = scanner.nextLine();
                        docteur.trouverFichePatient(nomPatient);
                    }
                }
            }

            else if (role == 2) {
                // Actions du secrétaire
                System.out.println("Choisissez : 1- Voir les fiches patients, 2- Gérer les rendez-vous");
                int actionSecretaire = scanner.nextInt();
                scanner.nextLine();

                if (actionSecretaire == 1){
                    // Voir les fiches patients
                    if (docteur.getFichesPatients().isEmpty()) {
                        System.out.println("Aucun patient dans la liste.");
                    } else {
                        System.out.print("Entrez le nom du patient pour afficher la fiche : ");
                        String nomPatient = scanner.nextLine();
                        docteur.trouverFichePatient(nomPatient);
                    }
                } else if (actionSecretaire == 2) {
                    System.out.println("1- Ajouter un rendez-vous, 2- Annuler un rendez-vous, 3- Voir les rendez-vous");
                    int actionRendezVous = scanner.nextInt();
                    scanner.nextLine();

                    if (actionRendezVous == 1) {
                        if (actionRendezVous == 1) {
                            System.out.print("Entrez le nom du patient : ");
                            String nom = scanner.nextLine();
                            System.out.print("Entrez le prénom du patient : ");
                            String prenom = scanner.nextLine();
                            System.out.print("Entrez le numéro de téléphone du patient : ");
                            String telephone = scanner.nextLine();
                            Patient nouveauPatient = new Patient(nom, prenom, telephone); // Create a new patient
                            System.out.print("Entrez la date du rendez-vous (AAAA-MM-JJ) : ");
                            String dateStr = scanner.nextLine();
                            Date date = formatDate.parse(dateStr);
                            secretaire.prendreRendezVous(nouveauPatient, date); // Schedule appointment for the new patient
                            System.out.println("Rendez-vous ajouté avec succès.");
                        }
                    } else if (actionRendezVous == 2) {
                        System.out.print("Entrez la date du rendez-vous à annuler (AAAA-MM-JJ) : ");
                        String dateStr = scanner.nextLine();
                        Date date = formatDate.parse(dateStr);
                        secretaire.annulerRendezVous(date);
                        System.out.println("Rendez-vous annulé avec succès.");
                    } else if (actionRendezVous == 3) {
                        List<RendezVous> rendezVous = secretaire.getRendezVous();
                        if (rendezVous.isEmpty()) {
                            System.out.println("Aucun rendez-vous planifié.");
                        } else {
                            System.out.println("Rendez-vous :");
                            for (RendezVous rdv : rendezVous) {
                                System.out.println("Patient : " + rdv.getPatient().nom + " " + rdv.getPatient().prenom + ", Date : " + rdv.getDate());
                            }
                        }
                    }
                }
            } else if (role == 0) {
                System.out.println("Merci d'avoir utilisé le système. Au revoir!");
                break;
            } else {
                System.out.println("Veuillez entrer une option valide.");
            }
        }
        scanner.close();
    }
}
