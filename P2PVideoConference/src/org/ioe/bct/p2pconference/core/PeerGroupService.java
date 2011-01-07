/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.jxta.credential.AuthenticationCredential;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;
import net.jxta.exception.PeerGroupException;
import net.jxta.exception.ProtocolNotSupportedException;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.impl.membership.PasswdMembershipService;
import net.jxta.impl.peergroup.StdPeerGroupParamAdv;
import net.jxta.membership.Authenticator;
import net.jxta.membership.MembershipService;
import net.jxta.peer.PeerInfoService;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.ModuleSpecID;
import net.jxta.platform.NetworkManager.ConfigMode;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;
/**
 *
 * @author Administrator
 */
public class PeerGroupService{
    
    
    private ArrayList<PeerGroup> discoveredGroups=new ArrayList<PeerGroup>();
    private ArrayList<PeerGroupID> discoveredGroupsIds=new ArrayList<PeerGroupID>();
    private ArrayList<PeerGroupAdvertisement> myCreatedGroups=new ArrayList<PeerGroupAdvertisement>();
    private ArrayList<PeerAdvertisement> discoveredPeers=new ArrayList<PeerAdvertisement>();
    private String message="";
    public PeerGroup createPeerGroup(PeerGroup rootPeerGroup,String groupName,String login,String passwd) throws MalformedURLException, UnknownServiceException
    {
        // - Create the Peer Group by doing the following:
        // - Create a Peer Group Module Implementation Advertisement and publish it
        // - Create a Peer Group Adv and publish it
        // - Create a Peer Group from the Peer Group Adv and return this object
        PeerGroup newPeerGroup=null;
        PeerGroupAdvertisement newPeerGroupAdvertisement;
        // Create the PeerGroup Module Implementation Adv
        ModuleImplAdvertisement passwdMembershipModuleImplAdv;
        passwdMembershipModuleImplAdv = this.createPasswdMembershipPeerGroupModuleImplAdv(rootPeerGroup);
        // Publish it in the parent peer group
        DiscoveryService rootPeerGroupDiscoveryService =
                rootPeerGroup.getDiscoveryService();
        try {
            rootPeerGroupDiscoveryService.publish(
                    passwdMembershipModuleImplAdv,
                    PeerGroup.DEFAULT_LIFETIME,
                    PeerGroup.DEFAULT_EXPIRATION);
            rootPeerGroupDiscoveryService.remotePublish(
                    passwdMembershipModuleImplAdv,
                    PeerGroup.DEFAULT_EXPIRATION);
        }
        catch (java.io.IOException e) {
            System.err.println("Can't Publish passwdMembershipModuleImplAdv");
            System.exit(1);
        }
        // Now, Create the Peer Group Advertisement
        newPeerGroupAdvertisement =
                this.createPeerGroupAdvertisement(passwdMembershipModuleImplAdv,groupName,login,passwd);
        // Publish it in the parent peer group
        System.out.println(newPeerGroupAdvertisement.toString());
        myCreatedGroups.add(newPeerGroupAdvertisement);
        try {
            rootPeerGroupDiscoveryService.publish(
                        newPeerGroupAdvertisement,
                        PeerGroup.DEFAULT_LIFETIME,
                        PeerGroup.DEFAULT_EXPIRATION);
            rootPeerGroupDiscoveryService.remotePublish(
                        newPeerGroupAdvertisement,
                        PeerGroup.DEFAULT_EXPIRATION);
             
        }
        catch (java.io.IOException e) {
            System.err.println("Can't Publish satellaPeerGroupAdvertisement");
            System.exit(1);
        }
        // Finally Create the Peer Group
        if (newPeerGroupAdvertisement == null) {
                System.err.println("satellaPeerGroupAdvertisement is null");
        }
        try {
            newPeerGroup = rootPeerGroup.newGroup(newPeerGroupAdvertisement);
        }
        catch (net.jxta.exception.PeerGroupException e) {
            System.err.println("Can't create Satella Peer Group from Advertisement");
            e.printStackTrace();
            return null;
        }
        return newPeerGroup;

    }

    public void publishAdvertisement(PeerGroup netPeerGroup)
    {
        DiscoveryService rootPeerGroupDiscoveryService =
                netPeerGroup.getDiscoveryService();
        if(!myCreatedGroups.isEmpty())
        {
           Iterator<PeerGroupAdvertisement> itr=myCreatedGroups.iterator();
           while(itr.hasNext())
           {
                rootPeerGroupDiscoveryService.remotePublish(
                        itr.next(),
                        PeerGroup.DEFAULT_EXPIRATION);
           }
        }
    }

    private PeerGroupAdvertisement createPeerGroupAdvertisement(ModuleImplAdvertisement passwdMembershipModuleImplAdv,String groupName,String login,String passwd)
    {
        PeerGroupAdvertisement newPeerGroupAdvertisement=(PeerGroupAdvertisement)AdvertisementFactory.newAdvertisement(PeerGroupAdvertisement.getAdvertisementType());
        newPeerGroupAdvertisement.setPeerGroupID(IDFactory.newPeerGroupID());
        newPeerGroupAdvertisement.setModuleSpecID(
        passwdMembershipModuleImplAdv.getModuleSpecID());
        newPeerGroupAdvertisement.setName(groupName);
        newPeerGroupAdvertisement.setDescription("Peer Group using Password Authentication");
        // Now create the Structured Document Containing the login and
        // passwd informations. Login and passwd are put into the Param
        // section of the peer Group
        if (login!=null) {
            StructuredTextDocument loginAndPasswd=
                (StructuredTextDocument)
                StructuredDocumentFactory.newStructuredDocument(
                new MimeMediaType("text/xml"),"Parm");
                String loginAndPasswdString= login+":"+
                PasswdMembershipService.makePsswd(passwd)+":";
                TextElement loginElement = loginAndPasswd.createElement(
                "login",loginAndPasswdString);
                loginAndPasswd.appendChild(loginElement);
            // All Right, now that loginAndPasswdElement
            // (The strucuted document
            // that is the Param Element for The PeerGroup Adv
            // is done, include it in the Peer Group Advertisement
            newPeerGroupAdvertisement.putServiceParam(
            PeerGroup.membershipClassID,loginAndPasswd);
            }
        return newPeerGroupAdvertisement;
        
    }
    
    private ModuleImplAdvertisement createPasswdMembershipPeerGroupModuleImplAdv(PeerGroup rootPeerGroup) throws MalformedURLException, UnknownServiceException
    {
        ModuleImplAdvertisement allPurposePeerGroupImplAdv=null;
        try {
            allPurposePeerGroupImplAdv=rootPeerGroup.getAllPurposePeerGroupImplAdvertisement();
        }
        catch (java.lang.Exception e) {
            System.err.println("Can't Execute: getAllPurposePeerGroupImplAdvertisement();");
            System.exit(1);
        }
        ModuleImplAdvertisement passwdMembershipPeerGroupModuleImplAdv = allPurposePeerGroupImplAdv;
        ModuleImplAdvertisement passwdMembershipServiceModuleImplAdv = null;
        StdPeerGroupParamAdv passwdMembershipPeerGroupParamAdv = null;
            passwdMembershipPeerGroupParamAdv =
                new StdPeerGroupParamAdv(
                allPurposePeerGroupImplAdv.getParam());
        HashMap allPurposePeerGroupServicesHashtable = (HashMap) passwdMembershipPeerGroupParamAdv.getServices();
        Iterator allPurposePeerGroupServicesEnumeration = allPurposePeerGroupServicesHashtable.keySet().iterator();
        boolean membershipServiceFound=false;
        while ((!membershipServiceFound) && (allPurposePeerGroupServicesEnumeration.hasNext())) {
        Object allPurposePeerGroupServiceID = allPurposePeerGroupServicesEnumeration.next();
        if (allPurposePeerGroupServiceID.equals(PeerGroup.membershipClassID)) {
        // allPurposePeerGroupMemershipServiceModuleImplAdv is
        // the all Purpose Mermbership Service for the all
        // purpose Peer Group Module Impl adv
        ModuleImplAdvertisement allPurposePeerGroupMemershipServiceModuleImplAdv= (ModuleImplAdvertisement) allPurposePeerGroupServicesHashtable.get(allPurposePeerGroupServiceID);
        //Create the passwdMembershipServiceModuleImplAdv
        passwdMembershipServiceModuleImplAdv=this.createPasswdMembershipServiceModuleImplAdv(allPurposePeerGroupMemershipServiceModuleImplAdv);
        //Remove the All purpose Membership Service implementation
        allPurposePeerGroupServicesHashtable.remove(allPurposePeerGroupServiceID);
        // And Replace it by the Passwd Membership Service
        // Implementation
        allPurposePeerGroupServicesHashtable.put(
                            PeerGroup.membershipClassID,
                            passwdMembershipServiceModuleImplAdv);
                            membershipServiceFound=true;
        // Now the Service Advertisements are complete. Let's
        // update the passwdMembershipPeerGroupModuleImplAdv by
        // Updating its param
        passwdMembershipPeerGroupModuleImplAdv.setParam(
            (Element) passwdMembershipPeerGroupParamAdv.getDocument(MimeMediaType.XMLUTF8));
        // Update its Spec ID This comes from the
        // Instant P2P PeerGroupManager Code (Thanks !!!!)
        if (!passwdMembershipPeerGroupModuleImplAdv.getModuleSpecID().equals(
        PeerGroup.allPurposePeerGroupSpecID)) {
        passwdMembershipPeerGroupModuleImplAdv.setModuleSpecID(IDFactory.newModuleSpecID(
        passwdMembershipPeerGroupModuleImplAdv.getModuleSpecID().getBaseClass()));
        } else {
           ID  passwdGrpModSpecID =
                IDFactory.fromURL(new URL("urn","","jxta:uuid-"+
                            "DeadBeefDeafBabaFeedBabe00000001" +"04" +"06"));
        passwdMembershipPeerGroupModuleImplAdv.
        setModuleSpecID((ModuleSpecID) passwdGrpModSpecID);
        } //End Else
        membershipServiceFound=true;
        } //end if (allPurposePeerGroupServiceID.
        // equals(PeerGroup.membershipClassID))
        }//end While
        return passwdMembershipPeerGroupModuleImplAdv;
    }

    private ModuleImplAdvertisement  createPasswdMembershipServiceModuleImplAdv(ModuleImplAdvertisement allPurposePeerGroupMemershipServiceModuleImplAdv) {
        //Create a new ModuleImplAdvertisement for the
        // Membership Service
        ModuleImplAdvertisement passwdMembershipServiceModuleImplAdv = (ModuleImplAdvertisement) AdvertisementFactory.newAdvertisement(ModuleImplAdvertisement.getAdvertisementType());
        passwdMembershipServiceModuleImplAdv.setModuleSpecID(PasswdMembershipService.passwordMembershipSpecID);
        passwdMembershipServiceModuleImplAdv.setCode(PasswdMembershipService.class.getName());
        passwdMembershipServiceModuleImplAdv.setDescription(" Module Impl Advertisement for the PasswdMembership Service");
        passwdMembershipServiceModuleImplAdv.setCompat(allPurposePeerGroupMemershipServiceModuleImplAdv.getCompat());
        passwdMembershipServiceModuleImplAdv.setUri(allPurposePeerGroupMemershipServiceModuleImplAdv.getUri());
        passwdMembershipServiceModuleImplAdv.setProvider(allPurposePeerGroupMemershipServiceModuleImplAdv.getProvider());
        return passwdMembershipServiceModuleImplAdv;
    }

    public PeerGroup discoverPeerGroup(PeerGroup netPeerGroup,PeerGroupID peerGroupID)
    {
         PeerGroup newPeerGroup;
        DiscoveryService netPeerGroupDiscoveryService = null;
       
        if (netPeerGroup !=null) {
            netPeerGroupDiscoveryService =
                    netPeerGroup.getDiscoveryService();

        } else {
            System.err.println("Can't join Peer Group since it's parent is null");
            System.exit(1);
        }
        boolean isGroupFound = false;
        Enumeration localPeerGroupAdvertisementEnumeration = null;
        PeerGroupAdvertisement newPeerGroupAdvertisement = null;
        while (!isGroupFound) {
            try {
                localPeerGroupAdvertisementEnumeration =
                        netPeerGroupDiscoveryService.getLocalAdvertisements(DiscoveryService.GROUP, "GID", peerGroupID.toString());
              
            } catch (java.io.IOException e) {
                System.out.println("Can't Discover Local Adv");
            }
            if (localPeerGroupAdvertisementEnumeration != null) {
                while (localPeerGroupAdvertisementEnumeration.hasMoreElements()) {
                    PeerGroupAdvertisement pgAdv = null;
                    pgAdv = (PeerGroupAdvertisement) localPeerGroupAdvertisementEnumeration.nextElement();

        if (pgAdv.getPeerGroupID().equals(peerGroupID)) {
            newPeerGroupAdvertisement=pgAdv;
            isGroupFound=true;
            break;
                    }
                }
            }
            try {
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
            }
        }
        try {
            newPeerGroup = netPeerGroup.newGroup(newPeerGroupAdvertisement);
        }
        catch (net.jxta.exception.PeerGroupException e) {
            System.err.println("Can't create Peer Group from Advertisement");
            e.printStackTrace();
            return null;
        }
        return newPeerGroup;
    }

//----------------------------------------------------------------------------------------------------//
//----------------------------------------------------------------------------------------------------//
    public ArrayList<PeerGroup> discoverGroups(PeerGroup netPeerGroup)
    {

        System.out.println("Discovering the Peer Groups   " + netPeerGroup.getPeerGroupID().toString());
        ArrayList<PeerGroup> peerGroupArrayList=new ArrayList<PeerGroup>();
        DiscoveryService peerGroupsDiscoveryService=null;
        if(netPeerGroup!=null)
            peerGroupsDiscoveryService=netPeerGroup.getDiscoveryService();
        
        else
        {
            System.out.println("Cant discover peer groups since its parent group is null");
            System.exit(1);
        }
        Enumeration localPeerGroupAdvertisementEnumeration = null;

        try {

            class RemoteListener implements DiscoveryListener{
            ArrayList<PeerGroup> peerGroupList=new ArrayList<PeerGroup>();
            PeerGroup netPeerGroup=null;
            public RemoteListener(PeerGroup netPG)
            {
                netPeerGroup=netPG;
            }
            public ArrayList<PeerGroup> getPeerGroupList()
                {
                return peerGroupList;
            }
            public void discoveryEvent(DiscoveryEvent de) {
                    Enumeration enumm;
                    String str;
                    DiscoveryResponseMsg response=de.getResponse();
                    enumm=response.getResponses();
                    while(enumm.hasMoreElements())
                    {
                        try {
                            str = (String) enumm.nextElement();
                            PeerGroupAdvertisement remotePeerGroupAdv = (PeerGroupAdvertisement) AdvertisementFactory.newAdvertisement(MimeMediaType.XMLUTF8, new ByteArrayInputStream(str.getBytes()));
                            if(!discoveredGroupsIds.contains(remotePeerGroupAdv.getPeerGroupID()))
                            {
                                discoveredGroups.add(netPeerGroup.newGroup(remotePeerGroupAdv.getPeerGroupID()));
                                discoveredGroupsIds.add(remotePeerGroupAdv.getPeerGroupID());
                            }

                        } catch (IOException ex) {
                            Logger.getLogger(PeerGroupService.class.getName()).log(Level.SEVERE, null, ex);
                        }catch(PeerGroupException pgEx)
                        {
                            pgEx.printStackTrace();
                        }
                    }
        }

            }

            RemoteListener remoteListener=new RemoteListener(netPeerGroup);
            peerGroupsDiscoveryService.getRemoteAdvertisements(null, DiscoveryService.GROUP, null, null, 10,remoteListener);
            localPeerGroupAdvertisementEnumeration =
                        peerGroupsDiscoveryService.getLocalAdvertisements(DiscoveryService.GROUP, null, null);
                
            } catch (java.io.IOException e) {
                System.out.println("Can't Discover Local Adv");
            }
            if (localPeerGroupAdvertisementEnumeration != null) {
                while (localPeerGroupAdvertisementEnumeration.hasMoreElements()) {
                    PeerGroupAdvertisement pgAdv = null;
                    pgAdv = (PeerGroupAdvertisement) localPeerGroupAdvertisementEnumeration.nextElement();
                 

                try {
                   if(!discoveredGroupsIds.contains(pgAdv.getPeerGroupID()))
                   {
                       discoveredGroups.add(netPeerGroup.newGroup(pgAdv.getPeerGroupID()));
                       discoveredGroupsIds.add(pgAdv.getPeerGroupID()); 
                   }
                }
                catch (PeerGroupException ex) {
                    ex.printStackTrace();
                }
                }
        }
        System.out.println("Total Groups Discovered" +peerGroupArrayList.size());
        return discoveredGroups;
       }

//----------------------------------------------------------------------------------------------------//
//----------------------------------------------------------------------------------------------------//
    public void joinPeerGroup(PeerGroup peerGroup, String login, String passwd)
    {
        // Get the Heavy Weight Paper for the resume
        // Alias define the type of credential to be provided
        StructuredDocument creds = null;
        try {
        // Create the resume to apply for the Job
        // Alias generate the credentials for the Peer Group
        AuthenticationCredential authCred = new AuthenticationCredential(peerGroup, null, creds);

        // Create the resume to apply for the Job
        // Alias generate the credentials for the Peer Group
        MembershipService membershipService = peerGroup.getMembershipService();
        
        // Send the resume and get the Job application form
        // Alias get the Authenticator from the Authentication creds
        Authenticator auth = membershipService.apply(authCred);
        // Fill in the Job Application Form
        // Alias complete the authentication
        completeAuth(auth, login, passwd);
        // Check if I got the Job
        // Alias Check if the authentication that was submitted was
        //accepted.
        if (!auth.isReadyForJoin()) {
            System.out.println("Failure in authentication.");
            System.out.println("Group was not joined. Does not know how to complete authenticator");
            //JOptionPane.showMessageDialog(null, "Group was not joined. Login was incorrect.");
            message="Group was not joined. Login was incorrect";
        }
        // I got the Job, Join the company
        // Alias I the authentication I completed was accepted,
        // therefore join the Peer Group accepted.
        membershipService.join(auth);

        }
        catch (PeerGroupException e) {
            System.out.println("Failure in authentication.");
            System.out.println("Group was not joined. Login was incorrect.");
            message="Group was not joined. Login was incorrect";
            JOptionPane.showMessageDialog(null, "Error Joining Group. "+
                        e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        catch (ProtocolNotSupportedException pe){
             JOptionPane.showMessageDialog(null, "Error Joining Group. "+
                        pe.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
             return;
        }
        catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }
        catch (Exception ge){
             JOptionPane.showMessageDialog(null, "Error Joining Group. "+
                        ge.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }
public String getMessage()
    {
    return message;
}
//----------------------------------------------------------------------------------------------------//
//----------------------------------------------------------------------------------------------------//
    private void completeAuth(Authenticator auth, String login,
            String passwd) throws Exception {
        Method[] methods = auth.getClass().getMethods();
        Vector authMethods = new Vector();
        // Find out with fields of the application needs to be filled
        // Alias Go through the methods of the Authenticator class and
        // copy them sorted by name into a vector.
        for (int eachMethod = 0;
                eachMethod < methods.length; eachMethod++) {
            if (methods[eachMethod].getName().startsWith("setAuth")) {
                if (Modifier.isPublic(
                        methods[eachMethod].getModifiers())) {
                    // sorted insertion.
                    for (int doInsert = 0; doInsert <= authMethods.size(); doInsert++) {
                        int insertHere = -1;
                        if( doInsert == authMethods.size()) {
                            insertHere = doInsert;
                        } else {
                            if (methods[eachMethod].getName().compareTo(((Method) authMethods.elementAt(
                                    doInsert)).getName()) <= 0) {
                                insertHere = doInsert;
                            }
                        } // end else
                        if (-1 != insertHere) {
                            authMethods.insertElementAt(
                                    methods[eachMethod], insertHere);
                            break;
                        } // end if ( -1 != insertHere)
                    } // end for (int doInsert=0
                } // end if (modifier.isPublic
            } // end if (methods[eachMethod]
        } // end for (int eachMethod)
        Object[] AuthId = {login};
        Object[] AuthPasswd = {passwd};
        for (int eachAuthMethod = 0; eachAuthMethod < authMethods.size(); eachAuthMethod++) {
            Method doingMethod = (Method) authMethods.elementAt(eachAuthMethod);
            String authStepName = doingMethod.getName().substring(7);
            if (doingMethod.getName().equals("setAuth1Identity")) {
            // Found identity Method, providing identity
                doingMethod.invoke(auth, AuthId);
            } else if (doingMethod.getName().equals("setAuth2_Password")) {

            // Found Passwd Method, providing passwd
            doingMethod.invoke(auth, AuthPasswd);
        }
    }
}

//----------------------------------------------------------------------------------------------------//
//----------------------------------------------------------------------------------------------------//

    public ArrayList<PeerAdvertisement> discoverPeerInGroup(PeerGroup peerGroup)
    {
        ArrayList<PeerAdvertisement> peerAdvArrayList=new ArrayList<PeerAdvertisement>();
        DiscoveryService disS=peerGroup.getDiscoveryService();
          Enumeration localAds=null;
            try {
                 localAds=disS.getLocalAdvertisements(DiscoveryService.PEER, null, null);


                 class RemoteListener implements DiscoveryListener{
                 private ArrayList<PeerAdvertisement> peerAdvList=new ArrayList<PeerAdvertisement>();

                    public ArrayList<PeerAdvertisement> getPeerAdvList()
                     {
                        return peerAdvList;
                    }

                    public void discoveryEvent(DiscoveryEvent de) {
                       // throw new UnsupportedOperationException("Not supported yet.");
                       DiscoveryResponseMsg response=de.getResponse();
                       Enumeration enumm=response.getResponses();
                       String str="";
                       while(enumm.hasMoreElements())
                       {
                        try {
                            str = (String) enumm.nextElement();
                            PeerAdvertisement remotePeerAdv = (PeerAdvertisement) AdvertisementFactory.newAdvertisement(MimeMediaType.XMLUTF8, new ByteArrayInputStream(str.getBytes()));
                            if(!discoveredPeers.contains(remotePeerAdv))
                                discoveredPeers.add(remotePeerAdv);
                        } catch (IOException ex) {
                            Logger.getLogger(PeerGroupService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                       }
                    }

                 }
                 
                 RemoteListener remoteListener=new RemoteListener();
                 disS.getRemoteAdvertisements(null,DiscoveryService.PEER, null, null, 10,remoteListener);
                 
            } catch (IOException ex) {
                Logger.getLogger(PeerGroupService.class.getName()).log(Level.SEVERE, null, ex);
            }
            while(localAds.hasMoreElements())
            {
                PeerAdvertisement pAdv=null;
                pAdv=(PeerAdvertisement)localAds.nextElement();
                if(!discoveredPeers.contains(pAdv))
                    discoveredPeers.add(pAdv);
                System.out.println("At Peer Group Service :" +pAdv.getName());
            }
          return discoveredPeers;
    }

    
//----------------------------------------------------------------------------------------------------//
//----------------------------------------------------------------------------------------------------//
  

   
    
}
