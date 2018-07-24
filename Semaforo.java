import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.* ;
import jade.lang.acl.ACLMessage;


public class Semaforo extends Agent {


    private int verde = 0;
    private int amarelo = 2000;
    private int vermelho = 0;
    private long tempoVerde = 0;
    private long tempoAmarelo = 0;
    private long tempoVermelho = 0 ;
    private long sTime;
    static long tempoInicial = System.currentTimeMillis();
    private static int  contador = 0;

    public long getTempoInicial() {
        return tempoInicial;
    }

    public void setTempoInicial(long tempoInicial) {
        this.tempoInicial = tempoInicial;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        if (contador==0){
            Semaforo.contador=0;
        }else{
            Semaforo.contador = Semaforo.contador + contador;
        }
    }

    public long getsTime() {
        return sTime;
    }

    public void setsTime(long sTime) {
        this.sTime = sTime;
    }

    public long getTempoVerde() {
        return tempoVerde;
    }

    public void setTempoVerde(long tempoVerde) {
        this.tempoVerde = tempoVerde;
    }

    public long getTempoAmarelo() {
        return tempoAmarelo;
    }

    public void setTempoAmarelo(long tempoAmarelo) {
        this.tempoAmarelo = tempoAmarelo;
    }

    public long getTempoVermelho() {
        return tempoVermelho;
    }

    public void setTempoVermelho(long tempoVermelho) {
        this.tempoVermelho = tempoVermelho;
    }

    public int isVerde() {
        return this.verde;
    }

    public void setVerde(int verde) {
        this.verde = verde;
    }

    public int isAmarelo() {
        return this.amarelo;
    }

    public void setAmarelo(int amarelo) {
        this.amarelo = amarelo;
    }

    public int isVermelho() {
        return this.vermelho;
    }

    public void setVermelho(int vermelho) {
        this.vermelho = vermelho;
    }


    protected  void setup(){
        addBehaviour(new CyclicBehaviour( this){
            @Override
            public void action(){
                    ACLMessage msg = myAgent.receive();
                        if (msg != null) {
                            String content = msg.getContent();
                            String ontology = msg.getOntology();
                            if ( ontology . equalsIgnoreCase ( "+verde-vermelho" )) {
                                //TO DO
                                if(getContador()==2){setContador(0);}
                                setsTime(Long.valueOf(content));
                                System.out.println("O Agente " + msg.getSender().getName() + " manda"+this.getAgent().getName()+" aumentar o tempo verde e diminuir vermelho com sTime = "+content+"segundos");
                                //converter content pra inteiro e incrementar e decrementar os valores
                                setTempoVerde((long) (getsTime()*0.6));
                                setTempoVermelho((long) (getsTime()*0.25));
                                 setTempoAmarelo((long) (getsTime()*0.15));
                              //  setTempoVerde(getsTime()+getTempoAmarelo());
                               // setTempoVermelho(getsTime()-getTempoAmarelo());
                                setContador(1);
                            }else if( ontology . equalsIgnoreCase ( "-verde+vermelho" )){
                                if(getContador()==2){setContador(0);}
                                setsTime(Long.valueOf(content));
                                System.out.println("O Agente " + msg.getSender().getName() + " manda"+this.getAgent().getName()+" diminuir o tempo verde e aumentar vermelho com sTime = "+content+"segundos");
                               setTempoVermelho((long) (getsTime()*0.8));
                               setTempoVerde((long)(getsTime()*0.1));
                               setTempoAmarelo((long)(getsTime()*0.05));
                               // setTempoVerde(getsTime()-2*getTempoAmarelo());
                               // setTempoVermelho(2*getTempoAmarelo() + getsTime());

                                setContador(1);
                            }
                        }

                if(getContador()==2) {

                    if ("sinalHorizontal@127.0.0.1:1099/JADE".equals(this.getAgent().getName()) && getTempoAmarelo() != 0 && getTempoVermelho() != 0 && getTempoVerde() != 0) {
                        if (((System.currentTimeMillis() - getTempoInicial()) >= 1) && ((System.currentTimeMillis() - getTempoInicial()) < getTempoVermelho())) {
                            if(isVermelho()==0){
                                ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
                                msg1.addReceiver(new AID("interface", AID.ISLOCALNAME));
                                msg1.setLanguage("Português");
                                msg1.setOntology("Cor");
                                msg1.setContent("red");
                                myAgent.send(msg1);
                               // System.out.println(this.getAgent().getName() + "Sinal Vermelho");
                                setVerde(0);
                                setAmarelo(0);
                                setVermelho(1);
                            }
                        }
                        if (((System.currentTimeMillis() - getTempoInicial()) >= getTempoVermelho()) && ((System.currentTimeMillis() - getTempoInicial()) <= getTempoVerde() + getTempoVermelho())) {
                            if(isVerde()==0){
                                ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                                msg2.addReceiver(new AID("interface", AID.ISLOCALNAME));
                                msg2.setLanguage("Português");
                                msg2.setOntology("Cor");
                                msg2.setContent("verde");
                                myAgent.send(msg2);
                              //  System.out.println(this.getAgent().getName() + "Sinal Verde");
                                setVerde(1);
                                setAmarelo(0);
                                setVermelho(0);
                            }
                        }
                        if ((System.currentTimeMillis() - getTempoInicial()) > getTempoVerde() + getTempoVermelho()) {
                            if(isAmarelo()==0){
                                ACLMessage msg3 = new ACLMessage(ACLMessage.INFORM);
                                msg3.addReceiver(new AID("interface", AID.ISLOCALNAME));
                                msg3.setLanguage("Português");
                                msg3.setOntology("Cor");
                                msg3.setContent("amarelo");
                                myAgent.send(msg3);
                            //    System.out.println(this.getAgent().getName() + "Sinal Amarelo");
                                setVerde(0);
                                setAmarelo(1);
                                setVermelho(0);
                               if(getTempoVermelho()==(getsTime()*0.8)) setTempoVermelho(((long) (getsTime() * 0.85)));
                            }
                        }
                        if ((System.currentTimeMillis() - getTempoInicial()) > getTempoVerde() + getTempoAmarelo() + getTempoVermelho()) {
                            setTempoInicial(System.currentTimeMillis());
                        }

                    }

                    if ("sinalVertical@127.0.0.1:1099/JADE".equals(this.getAgent().getName()) && getTempoAmarelo() != 0 && getTempoVermelho() != 0 && getTempoVerde() != 0) {
                        if (((System.currentTimeMillis() - getTempoInicial()) >= 1) && ((System.currentTimeMillis() - getTempoInicial()) < getTempoVerde())) {
                            if(isVerde()==0){
                                ACLMessage msg4 = new ACLMessage(ACLMessage.INFORM);
                                msg4.addReceiver(new AID("interface", AID.ISLOCALNAME));
                                msg4.setLanguage("Português");
                                msg4.setOntology("Cor");
                                msg4.setContent("verde");
                                myAgent.send(msg4);
                                // System.out.println(this.getAgent().getName()+ " Sinal Verde");
                                setVerde(1);
                                setAmarelo(0);
                                setVermelho(0);
                            }

                        }
                        if (((System.currentTimeMillis() - getTempoInicial()) >= getTempoVerde()) && ((System.currentTimeMillis() - getTempoInicial()) <= getTempoVerde() + getTempoAmarelo())) {
                            if(isAmarelo()==0){
                                ACLMessage msg5 = new ACLMessage(ACLMessage.INFORM);
                                msg5.addReceiver(new AID("interface", AID.ISLOCALNAME));
                                msg5.setLanguage("Português");
                                msg5.setOntology("Cor");
                                msg5.setContent("amarelo");
                                myAgent.send(msg5);
                                // System.out.println(this.getAgent().getName() + " Sinal Amarelo");
                                setVerde(0);
                                setAmarelo(1);
                                setVermelho(0);
                                if(getTempoVermelho()==(getsTime()*0.8)) setTempoVermelho(((long) (getsTime() * 0.85)));
                            }
                        }
                        if ((System.currentTimeMillis() - getTempoInicial()) > getTempoVerde() + getTempoAmarelo()) {
                            if(isVermelho()==0){
                                ACLMessage msg6 = new ACLMessage(ACLMessage.INFORM);
                                msg6.addReceiver(new AID("interface", AID.ISLOCALNAME));
                                msg6.setLanguage("Português");
                                msg6.setOntology("Cor");
                                msg6.setContent("red");
                                myAgent.send(msg6);
                               // System.out.println(this.getAgent().getName()+ " Sinal Vermelho");
                                setVerde(0);
                                setAmarelo(0);
                                setVermelho(1);
                            }
                        }
                        if ((System.currentTimeMillis() - getTempoInicial()) > getTempoVerde() + getTempoAmarelo() + getTempoVermelho()) {
                            setTempoInicial(System.currentTimeMillis());
                        }

                    }
                }

            }
        });

    }
}
