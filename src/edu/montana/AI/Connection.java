package edu.montana.AI;

public class Connection {
    public Boolean status;
    public Region connectedRegion1;
    public Region connectedRegion2;


    public Connection(Boolean status, Region connectedRegion1, Region connectedRegion2) {
        this.status = status;
        this.connectedRegion1 = connectedRegion1;
        this.connectedRegion2 = connectedRegion2;
    }

    public Boolean connectionCorrect(){
        if (this.connectedRegion1.color.equals(this.connectedRegion2.color)||this.connectedRegion1.color == ""||this.connectedRegion2.color == ""){
            this.status=Boolean.FALSE;
            return this.status;
        }
        this.status=Boolean.TRUE;
        return this.status;

    }
}
