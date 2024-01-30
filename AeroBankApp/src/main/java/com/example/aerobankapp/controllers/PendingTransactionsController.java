package com.example.aerobankapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/pending", method=RequestMethod.GET)
@CrossOrigin(origins="http://localhost:3000")
public class PendingTransactionsController
{

}
