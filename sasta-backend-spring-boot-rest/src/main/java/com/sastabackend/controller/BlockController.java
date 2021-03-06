package com.sastabackend.controller;

import com.sastabackend.domain.Blocks;
import com.sastabackend.domain.ResponseModel;
import com.sastabackend.service.block.BlockService;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by SARVA on 07/Nov/2015.
 */
@RestController
@RequestMapping("/api/block")
public class BlockController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockController.class);

    @Autowired
    private BlockService blockService;
    @Inject
    public BlockController(final BlockService blockService) {
        this.blockService = blockService;
    }

    @ApiOperation(value = "Create Block", response = ResponseModel.class, httpMethod = "POST")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseModel AddBlock(@RequestBody final Blocks block){
       return  blockService.Add(block.getBlockName(),block.getDescription(),block.getDistrictID(),block.getCreatedBy());
    }

    @ApiOperation(value = "Update Block", response = ResponseModel.class, httpMethod = "POST")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseModel UpdateBlock(@RequestBody final Blocks block){
        return  blockService.Update(block.getBlockID(), block.getBlockName(),block.getDescription(), block.getDistrictID(), block.getModifiedBy(), block.getIsActive());
    }

    @ApiOperation(value = "Read Block List", response = ResponseModel.class, httpMethod = "GET")
    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    public ResponseModel getBlockList(){
        return  blockService.getList();
    }
}
