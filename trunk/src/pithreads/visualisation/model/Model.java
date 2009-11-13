/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.model;

import java.util.ArrayList;
import pithreads.visualisation.tools.Triplet;
import pithreads.visualisation.view.Color;

/**
 *
 * @author mathurin
 */
public class Model implements IModel {

    private int milliSpeed;
    private int movementSpeed;
    private ArrayList<ISphere> sphereList;
    private ArrayList<IMovement> movementList;
    private ArrayList<ICube> cubeList;
    private ArrayList<IArrow> arrowList;
    private boolean changed;
    private float rayonOfSpherePositionSphere;
    private float rayonOfCirclePositionSphere;
    private float rayonOfSpherePositionCube;
    private float rayonOfCirclePositionCube;
    private int reOrganisationSpeed;

    public Model() {
//        sphereList = (ArrayList<ISphere>) Collections.synchronizedList(new ArrayList<ISphere>());
        sphereList = new ArrayList<ISphere>();
        movementList = new ArrayList<IMovement>();
        cubeList = new ArrayList<ICube>();
        arrowList = new ArrayList<IArrow>();
        milliSpeed = 600;
        movementSpeed = 10000;
        changed = false;
        rayonOfSpherePositionSphere = 2.0f;
        rayonOfCirclePositionSphere = 2.0f;
        rayonOfCirclePositionCube = 0.4f;
        rayonOfSpherePositionCube = 0.4f;
        reOrganisationSpeed = 3;
    }

    public IArrow findArrow(String idArrow) {
        for (int i = 0; i < arrowList.size(); i++) {
            if (arrowList.get(i).getId().equals(idArrow)) {
                return arrowList.get(i);
            }
        }
        return null;
    }

    public void removeArrow(String idArrow) {
        for (int i = 0; i < arrowList.size(); i++) {

            //  System.out.println("param "+ idArrow +" dans la liste "+arrowList.get(i).getId());
            if (arrowList.get(i).getId().equals(idArrow)) {
                arrowList.remove(i);
            }
        }
    }

    public ISphere findSphere(int idSphere) {
        for (int i = 0; i < sphereList.size(); i++) {

            if (idSphere == sphereList.get(i).getId()) {
                return sphereList.get(i);
            }
        }
        return null;
    }

    public IMovement findMovement(int idmove) {
        for (int i = 0; i < movementList.size(); i++) {
            if (movementList.get(i).getId() == idmove) {
                return movementList.get(i);
            }
        }
        return null;
    }

    public ICube findCube(int idCube) {
        for (int i = 0; i < cubeList.size(); i++) {
            if (idCube == cubeList.get(i).getId()) {
                return cubeList.get(i);
            }
        }
        return null;
    }

    public void addArrowToArrowList(IArrow arrow) {


        //FIXME: bizarre, rajout dune fleche pour un thread supprimé?
        ISphere sphere = findSphere(arrow.getIdSphere());

        if (sphere == null) {
            return;
        }
        ICube cube = findCube(arrow.getIdCube());

        if (arrow.getIsAnArrowIn()) {
            arrow.setTripletSource3D(cube.getPosX3D(), cube.getPosY3D(), cube.getPosZ3D());
            arrow.setTripletSource2D(cube.getPosX2D(), cube.getPosY2D(), cube.getPosZ2D());
            arrow.setTripletDest3D(sphere.getPosition3D().getX(), sphere.getPosition3D().getY(), sphere.getPosition3D().getZ());
            arrow.setTripletDest2D(sphere.getPosition2D().getX(), sphere.getPosition2D().getY(), sphere.getPosition2D().getZ());

        } else {
            arrow.setTripletDest3D(cube.getPosX3D(), cube.getPosY3D(), cube.getPosZ3D());
            arrow.setTripletDest2D(cube.getPosX2D(), cube.getPosY2D(), cube.getPosZ2D());
            arrow.setTripletSource3D(sphere.getPosition3D().getX(), sphere.getPosition3D().getY(), sphere.getPosition3D().getZ());
            arrow.setTripletSource2D(sphere.getPosition2D().getX(), sphere.getPosition2D().getY(), sphere.getPosition2D().getZ());
        }

        arrowList.add(arrow);
    }

    public void addSphereToSphereList(ISphere sphere) {

        this.sphereList.add(sphere);
    }

    public ArrayList<ISphere> getSphereList() {
        return this.sphereList;
    }

    public void addCubeToCubeList(ICube cube) {
        this.cubeList.add(cube);
    }

    public ArrayList<ICube> getCubeList() {
        return this.cubeList;
    }

    public ArrayList<IArrow> getArrowList() {
        return this.arrowList;
    }

    public boolean isAnyArrowForThisChannel(int idChannel) {
        boolean result = false;
        for (int i = 0; i < this.arrowList.size(); i++) {
            if (this.arrowList.get(i).getIdCube() == idChannel) {
                result = true;
            }
        }
        return result;
    }

    public void removeCube(int idChannel) {
        for (int i = 0; i < this.cubeList.size(); i++) {
            if (this.cubeList.get(i).getId() == idChannel) {

                cubeList.remove(cubeList.get(i));

            }
        }
    }

    public void addMovementToMovementList(int millispeed, int idThreadS, int idChannel, int idThreadDest, Color color) {
        ISphere sphere = findSphere(idThreadS);
        //FIXME: bizarre comme dab thread suppr
        if (sphere == null) {
            return;
        }

        Triplet source3D = new Triplet(sphere.getPosition3D());
        Triplet transition3D = this.findCube(idChannel).getPosition3D();
        Triplet dest3D = this.findSphere(idThreadDest).getPosition3D();

        Triplet source2D = new Triplet(sphere.getPosition2D());
        Triplet transition2D = this.findCube(idChannel).getPosition2D();
        Triplet dest2D = this.findSphere(idThreadDest).getPosition2D();

        IMovement move = new Movement(millispeed, source3D, transition3D, dest3D, source2D, transition2D, dest2D, color);
        this.movementList.add(move);
    }

    public ArrayList<IMovement> getMovementList() {
        return this.movementList;
    }

    public void removeLastMove() {
        this.movementList.remove(this.movementList.size() - 1);
    }

    public int getMovementSpeed() {
        return this.movementSpeed;
    }

    public int getMilliSpeed() {
        return this.milliSpeed;
    }

    public void setMovementSpeed(int speed) {
        this.movementSpeed = speed;
    }

    public void setMilliSpeed(int speed) {
        this.milliSpeed = speed;
    }

    public boolean hasChanged() {
        return this.changed;
    }

    public void setChanged(boolean b) {
        this.changed = b;
    }

    public float getRayonOfSpherePositionSphere() {
        return this.rayonOfSpherePositionSphere;
    }

    public void setRayonOfSpherePositionSphere(float f) {
        this.rayonOfSpherePositionSphere = f;
    }

    public float getRayonOfCirclePositionSphere() {
        return this.rayonOfCirclePositionSphere;
    }

    public void setRayonOfCirclePositionSphere(float f) {
        rayonOfCirclePositionSphere = f;
    }

    public void removeSphere(int idSphere) {

        sphereList.remove(findSphere(idSphere));
    }

    public float getRayonOfCirclePositionCube() {
        return this.rayonOfCirclePositionCube;
    }

    public float getRayonOfSpherePositionCube() {
        return this.rayonOfSpherePositionCube;
    }

    public void setRayonOfCirclePositionCube(float f) {
        this.rayonOfCirclePositionCube = f;
    }

    public void setRayonOfSpherePositionCube(float f) {
        this.rayonOfSpherePositionCube = f;
    }

    public void resetOverlignedSphere() {
        for (int i = 0; i < this.sphereList.size(); i++) {
            sphereList.get(i).overligne(false);
        }
    }

    public void resetOverlignedChannel() {
        for (int i = 0; i < this.cubeList.size(); i++) {
            cubeList.get(i).setOverligne(false);
        }
    }

    public void clearAll() {
        this.movementList.clear();
        this.cubeList.clear();
        this.sphereList.clear();
        this.arrowList.clear();
    }

    public void setReOrganisationSpeed(int parseInt) {
        this.reOrganisationSpeed = parseInt;
    }

    public int getReOrganisationSpeed() {
        return this.reOrganisationSpeed;
    }
}
