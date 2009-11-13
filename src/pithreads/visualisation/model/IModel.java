/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.model;

import java.util.ArrayList;
import pithreads.visualisation.view.Color;

/**
 *
 * @author mathurin
 */
public interface IModel {


    public void addMovementToMovementList(int millispeed,int idThreadS, int idChannel, int idThreadDest, Color color);

    public void clearAll();

    public ArrayList<IMovement> getMovementList();

    public float getRayonOfCirclePositionCube();

    public float getRayonOfCirclePositionSphere();

    public float getRayonOfSpherePositionCube();

    public float getRayonOfSpherePositionSphere();

    public int getReOrganisationSpeed();

    public boolean hasChanged();

    public void removeSphere(int idSphere);

    public void resetOverlignedChannel();

    public void resetOverlignedSphere();

    public void setChanged(boolean b);

    public boolean isAnyArrowForThisChannel(int idChannel);

    public void removeArrow(String idArrow);

    public void addCubeToCubeList(ICube cube);

    public void addSphereToSphereList(ISphere sphere);

    public ISphere findSphere(int idSphere);

    public IMovement findMovement(int idmove);

    public ICube findCube(int idCube);

    public ArrayList<IArrow> getArrowList();

    public ArrayList<ICube> getCubeList();

    public void addArrowToArrowList(IArrow arrow);

    public IArrow findArrow(String idArrow);

    public ArrayList<ISphere> getSphereList();

    public void removeCube(int idChannel);

    public void removeLastMove();

    public int getMovementSpeed();

    public void setMovementSpeed(int speed);

    public int getMilliSpeed();

    public void setMilliSpeed(int speed);

    public void setRayonOfCirclePositionCube(float f);

    public void setRayonOfCirclePositionSphere(float f);

    public void setRayonOfSpherePositionCube(float f);

    public void setRayonOfSpherePositionSphere(float f);

    public void setReOrganisationSpeed(int parseInt);


}
