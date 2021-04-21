package it.polito.tdp.nobel.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	private List<Esame> soluzioneMigliore; // così da non doverla inserire negli argomenti della funzione ricorsiva
	private double mediaSoluzioneMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza = dao.getTuttiEsami();
	}
	
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		List<Esame> parziale = new ArrayList<Esame>();
		//meglio inizializzarla qui tale che ad ogni uso si azzera
		soluzioneMigliore= new ArrayList<>();
		mediaSoluzioneMigliore=0;
		
		//cerca(parziale, 0, numeroCrediti);
		cerca(parziale, 0, numeroCrediti);
		
		return soluzioneMigliore;	
	}

	/*Complessità N!*/
	private void cerca(List<Esame> parziale, int L, int m) {
		// casi terminali
		
		int crediti = sommaCrediti(parziale);
		//System.out.println("L="+L+" "+parziale);
		if(crediti>m) {
			return;
		}else if(crediti==m) {
			double media = calcolaMedia(parziale);
			if(media>mediaSoluzioneMigliore) {
				soluzioneMigliore=new ArrayList<>(parziale);
				mediaSoluzioneMigliore=media;
			}
			return;
		}
		
		//1)  L=N--> non ci sono più esami da aggiungere
		if(L== partenza.size()) {
			return;
		}
		
		//genero i sotto_problemi
		/*for (Esame e: partenza) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca(parziale, L+1,m);
				parziale.remove(e); //backtracking
			}
		}*/
		
		//non è ancora perfetto perchè controllo i>=L non evita tutti i casi duplicati
		/*for(int i=0;i<partenza.size();i++) {
			
			if(!parziale.contains(partenza.get(i))&& i>=L) {
				parziale.add(partenza.get(i));
				cerca(parziale,L+1,m);
				parziale.remove(partenza.get(i)); //causa problemi
				
			}
		}*/
		
		int lastIndex=0; //cosi da scegliere sempre esami in ordine
		if(parziale.size()>0) {
			lastIndex=partenza.indexOf(parziale.get(parziale.size()-1));
		}
		
		for (int i=lastIndex;i<partenza.size();i++) {
			if(!parziale.contains(partenza.get(i))) {
				parziale.add(partenza.get(i));
				cerca(parziale,L+1,m);
				parziale.remove(partenza.get(i));
				
			}
		}
	}

	//Complessità di 2^N
	private void cerca2(List<Esame> parziale, int L, int m) {
		// casi terminali
		
		int crediti = sommaCrediti(parziale);
				
		if(crediti>m) {
			return;
		}else if(crediti==m) {
			double media = calcolaMedia(parziale);
			if(media>mediaSoluzioneMigliore) {
				soluzioneMigliore=new ArrayList<>(parziale);
				mediaSoluzioneMigliore=media;
			}
			return;
		}
				
		//1)  L=N--> non ci sono più esami da aggiungere
		if(L== partenza.size()) {
			return;
		}
		
		//generazione sotto_problemi
		//partenza[L] è da aggiungere oppure no? Provo entrambe
		parziale.add(partenza.get(L));
		cerca2(parziale,L+1,m);
		parziale.remove(partenza.get(L));
		cerca2(parziale,L+1,m);
	}
	
	public double calcolaMedia(List<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(List<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
